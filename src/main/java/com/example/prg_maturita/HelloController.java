package com.example.prg_maturita;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.control.Button;

import java.util.ArrayList;

public class HelloController {
    @FXML
    private Label welcomeText;
    @FXML
    private GridPane deska;


    Tile[][] tiles = new Tile[8][8];
    int[][] directions = new int[4][7]; //0=leva nahhore / 1=vpravo nahore / 2=vlevo dole / 3=vpravo dole
    ArrayList<Tile> jumpableTiles = new ArrayList<>();
    ArrayList<Tile> forcedTiles = new ArrayList<>();

    private Tile checkerTile;
    private Tile destinationTile;

    private int NORMAL = 0; // normální tah, hraac hybe figurkama a nikoho nepreskakuje
    private int FORCED_PICK_PIECE = 1; // detekovan forced jump, hrac musi vybrtat se kterou figurkou chce jump provest
    private int FORCED_PICK_TARGET = 2; // hrac uz figurku vybral, ted vybira kam s figurkou bude tahnout
    private int gameState = NORMAL; //


    //private boolean LIGHT = false;
    //private boolean DARK = true;
    //private boolean playerColour = LIGHT;


////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //vygeneruje policka do listu tiles
    public void generateTiles(){
    int id = 0;
    boolean isDark;

        for(int row = 0; row < 8; row++){
            for(int col = 0; col < 8; col++) {
                isDark = false;

                if((col+row+1)%2 == 0) {
                    isDark = true;
                }
                tiles[row][col] = new Tile(row*8+col, row, col, isDark);

            }
        }

    }

    //zobrazi policka
    public void displayTiles(){
        for (Tile[] row : tiles) {
            for (Tile t : row) {

                Button btn = t.getButton();
                btn.setOnAction( e -> handleTileClick(t));
                deska.add(btn, t.getCol(), t.getRow());
            }
        }

    }

    //vypne svetle policka/tlacitka
    public void disableLightTiles(){
        for (Tile[] row : tiles) {
            for (Tile t : row) {
                Button btn = t.getButton();
                if(!t.getDarkTile()){
                    btn.setDisable(true);
                    btn.setStyle("-fx-opacity: 1.0;");
                }

            }
        }
    }

////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //pokud user klikme na políčko...
    @FXML
    protected void handleTileClick(Tile tile) {
        //System.out.println("Tile Clicked");
        //tile.outlineTile();
        if (gameState == FORCED_PICK_PIECE) { // hrac musi vybrat s jakou figurkou udela forced jump - // presmerovani z startturn

            if (forcedTiles.contains(tile)) { // pokud klikl na jeden z forced podsvicenych figurek
                checkerTile = tile;
                clearAllOutlines(); // odoutlinene vsechny popdsvicene figurky
                checkerTile.outlineTile();
                addJumpableForcedTiles(checkerTile); // spocita a prida do jumpable tiles cile/mozna policka
                outlineTiles(jumpableTiles); // otlninene policka kam muze hrac tahnout
                gameState = FORCED_PICK_TARGET; // zmeni se stav tahu
            }
            // pokud klikl na neco jineho nic se nestane

        }else if  (gameState == FORCED_PICK_TARGET) { // hrac musi vybrat na jeke policko udela forced jump

            if (jumpableTiles.contains(tile)) { // pokud klikne na policko
                jumpTile(checkerTile, tile); // tile je cil kam chci  skocit, checker tile pocatek aka figurka se kterou chci skocit
            } else { // pokud ne - zpatky na forced pick piece
                clearAllOutlines();
                //jumpableTiles.clear();
                //checkerTile = null;
                outlineTiles(forcedTiles);
                gameState = FORCED_PICK_PIECE;
            }

        }else{ // NORMAL - jen hybe jakoukoli figurkou

            if (jumpableTiles.contains(tile)) { //pokud klikne na označené pole

                clearAllOutlines();
                moveTile(checkerTile, tile); // tile je cil kam chci presunout, checker tile je pocatek aka figurka kterou chci presunout
                ukoncitKolo();

            } else { //pokud klikne an neoznacenou fugurku nebo na prazdny tile
                clearAllOutlines();
                jumpableTiles.clear(); // vymaze stare cile
                checkerTile = null;

                if (tile.getTileState()!=0 && tile.getDarkPlayer()==playerDark) { //pokiud tile neni prazdny a je to moje figurka
                    checkerTile = tile;
                    addJumpableTiles(checkerTile);
                    outlineTiles(jumpableTiles);
                }
            }

        }


    }

    public void startTurn() { //zzz
        //clearAllOutlines();
        //jumpableTiles.clear();
        checkerTile = null;

        findForcedTiles(); // prohleda pole a nalezy da do pole forcedtiles

        if (forcedTiles.isEmpty()) { // pokud nic nenaslo
            gameState = NORMAL; // normalni tah
        } else {// pokud ano
            outlineTiles(forcedTiles); // zobrazi to hracovi
            gameState = FORCED_PICK_PIECE; // hrac musi vybrat figurku se kterou forced jump udela
        }
    }


    public void outlineTiles(ArrayList<Tile> list) {
        for (Tile t : list) {
            t.outlineTile();
        }
    }


    public void clearAllOutlines(){
        for(Tile jumpable : jumpableTiles){
            jumpable.clearOutlineTile();
        }
        for (Tile forced : forcedTiles) {
            forced.clearOutlineTile();
        }
    }

    public void ukoncitKolo(){
        playerDark = !playerDark; // zmeni hrace
        startTurn(); // zacne dalsi kolo
    }


    public void jumpTile(Tile from, Tile to) {// pro preskoceni figurky
        clearAllOutlines(); // odoutlinene cilova policka
        jumpableTiles.clear(); // pro jistotu vymaze jumppabvle tiles

        Tile sebrany = tiles[(from.getRow() + to.getRow()) / 2][(from.getCol() + to.getCol()) / 2]; // operace vzdy vrati cislo mezi zacatkem a cilem

        moveTile(from, to);            // BOD 3: kámen se přesune

        sebrany.setTileState(0);       // přeskočený soupeř zmizí
        sebrany.setDarkPlayer(null);
        sebrany.updateTileImage();

        // overuje jestli ta sama figurka musi skakat znovu
        forcedTiles.clear();
        addJumpableForcedTiles(to); // ulozi forced tiles do seznamu jumpableTiles pokud nejaky je
        if (!jumpableTiles.isEmpty()) { // pokud jumpabletiles neni prazny aka pokud figurka musi skakat znovu
            jumpableTiles.clear(); // znovu vymaze policka z jumpable tiles

            forcedTiles.add(to); // prida figurku do seznamu forcedtiles
            outlineTiles(forcedTiles); // oznaci ji
            gameState = FORCED_PICK_PIECE;// prepna na game mod kde hrac musi vybrat s jakou figurkou skoci
        } else { // pokud nema dalsi forced jumps
            ukoncitKolo(); // konec tahu, zacne dalsi kolo
        }
    }


    public void moveTile(Tile from, Tile to) {

        to.setTileState(from.getTileState());
        to.setDarkPlayer(from.getDarkPlayer());
        to.updateTileImage();

        from.setTileState(0);
        from.setDarkPlayer(null);

        to.updateTileImage();
        from.updateTileImage();

        if (to.getRow() == 0 || to.getRow() == 7) {
            to.setTileState(2);
            to.updateTileImage();
        }
    }


    boolean playerDark = true;
    public void calculateDirections(Tile tile){
        for (int[] direction : directions) {
            java.util.Arrays.fill(direction, -1);
        }

        //leva nahore, prava nahore, leva dole, prava dole
        int[] krok = {-9, -7, 7, 9}; // o kolik se zmeni id
        int[] sloupec = {-1, 1, -1, 1}; // o kolik se zmeni sloupec

        for (int strana = 0; strana < 4; strana++) { //pro každou diagonalni stranu
            int id = tile.getId();// nacte se id aktualniho tilu
            int col = tile.getCol();//nacte se col tilu pro kontrolu

            for (int mistoVListu = 0; mistoVListu < 7; mistoVListu++) { // pro kazde misto 1-7 v jedne strane
                id = id + krok[strana]; //vypocita id
                col = col + sloupec[strana];

                if (id<0 || id>63 || col<0 || col>7) { // pokud je id mimo desku
                    //nic se nestane
                }else{ // pokud je validni
                    directions[strana][mistoVListu] = id; // ulozi se do directions
                }

            }
        }

    }

    public Tile getTileById(int id) { // returne tile s timto id
        return tiles[id / 8][id % 8]; //vypocita row a col na desce z id
    }

    public void addJumpableForcedTiles(Tile tile) { // zkontroluje  jestli dany tile musi udelat forced jump
        jumpableTiles.clear();
        calculateDirections(tile);  // spocita directions pro tuhle figurku
        boolean barvaHrace = tile.getDarkPlayer();

        if(tile.getTileState() == 1){ // pokud kamen
            // tmavy hrac jde nahoru (0 a 1)  svetly dolu (2 a 3)
            int prvniStrana = 0; // pokud tmavy
            if (!barvaHrace) {
                prvniStrana = 2; // pokud svetly
            }

            for (int strana = prvniStrana; strana < prvniStrana + 2; strana++) {
                int id1 = directions[strana][0]; // sousedni policko
                int id2 = directions[strana][1]; // policko za sousednim polickem

                if (id1!=-1 && id2!=-1) { // pokud jsou id na desce
                    Tile sousedni= getTileById(id1);
                    Tile zaSousednim = getTileById(id2);

                    // pukud je sousedni policko figruka a je soupere && pokud je policko za nim volne
                    if ((sousedni.getTileState()!=0 && sousedni.getDarkPlayer()!=barvaHrace) && (zaSousednim.getTileState()==0)) {
                        jumpableTiles.add(zaSousednim); // prida se do jumpable tiles - pouziva se primo pro zobrazeni-handletileclick, nebo neprimo pro zjisteni jestli ma figurka forced jump-findForcedTiles
                    }
                }
            }

        }else if(tile.getTileState() == 2){ // pokud dama
            //////dama dodelat
        }


    }


    public void findForcedTiles() { // projde celou desku a ulozi tile ktery musi udelat forced jump
        forcedTiles.clear();

        for (Tile[] row : tiles) { // projde row
            for (Tile t : row) { // projde col

                if (t.getTileState()!=0 && t.getDarkPlayer()==playerDark) { // pokud je to figurka a je hrace na tahu
                    addJumpableForcedTiles(t); // pridaji se policka do jumpable tiles - pouziva se na zjisteni jestli ma figurrka forced jump
                    if (!jumpableTiles.isEmpty()) { // pokud jumpable tiles nejsou prazda - neboli jestli ma policko forced jumable tiles
                        jumpableTiles.clear(); // list se vycisti
                        forcedTiles.add(t); // policko od ktereho se to pocita se prida do forced tiles - figurka
                    }
                }
            }
        }
    }






    public void addJumpableTiles(Tile tile) {
        jumpableTiles.clear();
        calculateDirections(tile);

        boolean barvaHrace = tile.getDarkPlayer();

        if(tile.getTileState() == 1) { // pokud kamen
            // tmavy hrac jde nahoru (0 a 1)  svetly dolu (2 a 3)
            int prvniStrana = 0; // pokud tmavy
            if (!barvaHrace) {
                prvniStrana = 2; // pokud svetly
            }

            for (int strana = prvniStrana; strana < prvniStrana + 2; strana++) {
                int id1 = directions[strana][0]; // prvni policko v tom smeru

                if (id1 != -1) { // pokud je id na desce
                    Tile to = getTileById(id1);

                    // pukud je sousedni policko figruka a je soupere && pokud je policko za nim volne
                    if (to.getTileState() == 0) {
                        jumpableTiles.add(to); // pridani do jumpable tiles
                    }
                }

            }

        }if(tile.getTileState() == 2){
            ///////dama dodelat
        }




    }

    /*
    public void addJumpableForcedTiles(Tile tile) {
        //jumpableTiles.clear(); - nemusi byt tady, je uz v hasforcedjump
        hasForcedJump(tile);
    }
    */


    @FXML
    public void initialize() {
        generateTiles();
        displayTiles();
        disableLightTiles();

        //zkouska
        // DOČASNÝ TEST (později smazat)
        tiles[4][3].setTileState(1);
        tiles[4][3].setDarkPlayer(false);
        tiles[4][3].updateTileImage();

        tiles[1][2].setTileState(0);          // uvolníme (1,2), aby se dalo skákat dál
        tiles[1][2].setDarkPlayer(null);
        tiles[1][2].updateTileImage();

        startTurn();
    }































}
