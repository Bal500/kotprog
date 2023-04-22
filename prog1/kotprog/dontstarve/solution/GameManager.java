package prog1.kotprog.dontstarve.solution;

import prog1.kotprog.dontstarve.solution.character.BaseCharacter;
import prog1.kotprog.dontstarve.solution.character.Character;
import prog1.kotprog.dontstarve.solution.character.actions.Action;
import prog1.kotprog.dontstarve.solution.exceptions.NotImplementedException;
import prog1.kotprog.dontstarve.solution.level.BaseField;
import prog1.kotprog.dontstarve.solution.level.Field;
import prog1.kotprog.dontstarve.solution.level.Level;
import prog1.kotprog.dontstarve.solution.utility.Position;

import java.util.Random;
import java.util.List;
import java.util.ArrayList;

/**
 * A játék működéséért felelős osztály.<br>
 * Az osztály a singleton tervezési mintát valósítja meg.
 */
public final class GameManager {
    /**
     * Tutorial mód tárolására szolgáló változó.
     */
    private boolean tutorial;

    /**
     * Az időt mérő egész értéket tároló változó.
     */
    private final int timeManager;

    /**
     * A pályát tároló lista.
     */
    private BaseField[][] fields;

    /**
     * Ez a változó jelzi, hogy a pálya betöltésre került-e már.
     */
    private boolean loaded;

    /**
     * A pályát tároló változó.
     */
    private Level level;

    /**
     * Ez a változó jelzi, hogy az emberi játékos csatlakozott-e a játékba.
     */
    private boolean playerJoined;

    /**
     * A karaktereket tároló lista.
     */
    private final List<BaseCharacter> characters;

    /**
     * Az emberi játékos karakter.
     */
    private String humanName;

    /**
     * Ez a változó jelzi, hogy a játék elkezdődött-e már.
     */
    private boolean gameStarted;

    /**
     * Az osztályból létrehozott egyetlen példány (nem lehet final).
     */
    private static GameManager instance = new GameManager();

    /**
     * Random objektum, amit a játék során használni lehet.
     */
    private final Random random = new Random();

    /**
     * Az osztály privát konstruktora.
     */
    private GameManager() {
        characters = new ArrayList<>();
        this.tutorial = false;
        this.loaded = false;
        this.gameStarted = false;
        this.playerJoined = false;
        this.timeManager = 0;
    }

    /**
     * Az osztályból létrehozott példány elérésére szolgáló metódus.
     *
     * @return az osztályból létrehozott példány
     */
    public static GameManager getInstance() {
        return instance;
    }

    /**
     * A létrehozott random objektum elérésére szolgáló metódus.
     *
     * @return a létrehozott random objektum
     */
    public Random getRandom() {
        return random;
    }

    /**
     * Egy karakter becsatlakozása a játékba.<br>
     * A becsatlakozásnak számos feltétele van:
     * <ul>
     *     <li>A pálya már be lett töltve</li>
     *     <li>A játék még nem kezdődött el</li>
     *     <li>Csak egyetlen emberi játékos lehet, a többi karaktert a gép irányítja</li>
     *     <li>A névnek egyedinek kell lennie</li>
     *     <li>A karaktereknek, ha lehetséges 50 egységnyire kell spawnolniuk egymástól!</li>
     * </ul>
     *
     * @param name a csatlakozni kívánt karakter neve
     * @param player igaz, ha emberi játékosról van szó; hamis egyébként
     * @return a karakter pozíciója a pályán, vagy (Integer.MAX_VALUE, Integer.MAX_VALUE) ha nem sikerült hozzáadni
     */
    public Position joinCharacter(String name, boolean player) {
        if (level == null || gameStarted || playerJoined && player || name.equals("")) {
            return new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }

        if (player) {
            playerJoined = true;
        }

        if (!characters.isEmpty()) {
            for (BaseCharacter character : characters) {
                if (character != null && character.getName().equals(name)) {
                    return new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
                }
            }
        }

        // Random cuccok adása

        int threshold = 50;
        boolean placed = false;
        while (!placed && threshold >= 5) {
            int x = random.nextInt(level.getWidth());
            int y = random.nextInt(level.getHeight());
            Position pos = new Position(x, y);
            boolean valid = true;
            for (BaseCharacter character : characters) {
                if (level.distance(character.getCurrentPosition(), pos) < threshold) {
                    valid = false;
                    break;
                }
            }
            if (valid && getField(x, y).isWalkable()) {
                BaseCharacter character = new Character(name, x, y);
                characters.add(character);
                placed = true;
                return new Position(x, y);
            } else {
                threshold -= 5;
            }
        }

        if (!placed) {
            return new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        } else if (!(characters.isEmpty())) {
            return characters.get(characters.size() - 1).getCurrentPosition();
        } else {
            return new Position(Integer.MAX_VALUE, Integer.MAX_VALUE);
        }        
    }

    /**
     * Egy adott nevű karakter lekérésére szolgáló metódus.<br>
     *
     * @param name A lekérdezni kívánt karakter neve
     * @return Az adott nevű karakter objektum, vagy null, ha már a karakter meghalt vagy nem is létezett
     */
    public BaseCharacter getCharacter(String name) {
        for (BaseCharacter character : characters) {
            if (character.getName().equals(name)) {
                if (character.getHp() > 0) {
                    return character;
                } else {
                    return null;
                }
            }
        }
        return null;
    }

    /**
     * Ezen metódus segítségével lekérdezhető, hogy hány karakter van még életben.
     *
     * @return Az életben lévő karakterek száma
     */
    public int remainingCharacters() {
        int remaining = 0;
        for (BaseCharacter character : characters) {
            if (character.getHp() > 0) {
                remaining++;
            }
        }
        return remaining;
    }

    /**
     * Ezen metódus segítségével történhet meg a pálya betöltése.<br>
     * A pálya betöltésének azelőtt kell megtörténnie, hogy akár 1 karakter is csatlakozott volna a játékhoz.<br>
     * A pálya egyetlen alkalommal tölthető be, később nem módosítható.
     *
     * @param level a fájlból betöltött pálya
     */
    public void loadLevel(Level level) {
        if (!loaded) {
            fields = new Field[level.getWidth()][level.getHeight()];
            this.level = level;
            if (level != null && !loaded && characters.isEmpty()) {
                for (int i = 0; i < level.getWidth(); i++) {
                    for (int j = 0; j < level.getHeight(); j++) {
                        fields[i][j] = new Field(level.getColor(i, j), i, j);
                    }
                }
                loaded = true;
            }
        }
    }

    /**
     * A pálya egy adott pozícióján lévő mező lekérdezésére szolgáló metódus.
     *
     * @param x a vízszintes (x) irányú koordináta
     * @param y a függőleges (y) irányú koordináta
     * @return az adott koordinátán lévő mező
     */
    public BaseField getField(int x, int y) {
        return fields[x][y];
    }

    /**
     * A játék megkezdésére szolgáló metódus.<br>
     * A játék csak akkor kezdhető el, ha legalább 2 karakter már a pályán van,
     * és közülük pontosan az egyik az emberi játékos által irányított karakter.
     *
     * @return igaz, ha sikerült elkezdeni a játékot; hamis egyébként
     */
    public boolean startGame() {
        if (!gameStarted) {
            int humanCount = 0;
            int botCount = 0;
            boolean hasComputerPlayer = false;
            boolean hasHumanPlayer = false;

            for (BaseCharacter character : characters) {
                if (character.getName().equals(humanName)) {
                    humanCount++;
                    hasHumanPlayer = true;
                } else {
                    botCount++;
                    hasComputerPlayer = true;
                }
            }

            if (!hasComputerPlayer || !hasHumanPlayer) {
                return false;
            }

            if (characters.size() == 2 && humanCount == 1 && botCount == 1) {
                gameStarted = true;
                return true;
            } else if (characters.size() > 2 && hasHumanPlayer && hasComputerPlayer) {
                gameStarted = true;
                return true;
            } else if (characters.size() < 2) {
                gameStarted = false;
                return false;
            }
        } else {
            gameStarted = false;
            return false;
        }
        return false;
    }


    /**
     * Ez a metódus jelzi, hogy 1 időegység eltelt.<br>
     * A metódus először lekezeli a felhasználói inputot, majd a gépi ellenfelek cselekvését végzi el,
     * végül eltelik egy időegység.<br>
     * Csak akkor csinál bármit is, ha a játék már elkezdődött, de még nem fejeződött be.
     *
     * @param action az emberi játékos által végrehajtani kívánt akció
     */
    public void tick(Action action) {
        throw new NotImplementedException();
    }

    /**
     * Ezen metódus segítségével lekérdezhető az aktuális időpillanat.<br>
     * A játék kezdetekor ez az érték 0 (tehát a legelső időpillanatban az idő 0),
     * majd minden eltelt időpillanat után 1-gyel növelődik.
     *
     * @return az aktuális időpillanat
     */
    public int time() {
        return timeManager;
    }

    /**
     * Ezen metódus segítségével lekérdezhetjük a játék győztesét.<br>
     * Amennyiben a játéknak még nincs vége (vagy esetleg nincs győztes), akkor null-t ad vissza.
     *
     * @return a győztes karakter vagy null
     */
    public BaseCharacter getWinner() {
        List<BaseCharacter> aliveCharacters = new ArrayList<>();
        for (BaseCharacter character : characters) {
            if (character.getHp() > 0) {
                aliveCharacters.add((BaseCharacter) character);
            }
        }

        if (aliveCharacters.size() == 1) {
            return aliveCharacters.get(0);
        }
        return null;
    }


    /**
     * Ezen metódus segítségével lekérdezhetjük, hogy a játék elkezdődött-e már.
     *
     * @return igaz, ha a játék már elkezdődött; hamis egyébként
     */
    public boolean isGameStarted() {
        return this.gameStarted;
    }

    /**
     * Ezen metódus segítségével lekérdezhetjük, hogy a játék befejeződött-e már.
     *
     * @return igaz, ha a játék már befejeződött; hamis egyébként
     */
    public boolean isGameEnded() {
        boolean playerAlive = false;
        boolean botAlive = false;
        for (BaseCharacter character : characters) {
            if (character.getHp() > 0) {
                if (character.getName() == humanName) {
                    if (character.getHp() > 0) {
                        playerAlive = true;
                    } else {
                        playerAlive = false;
                    }
                } else {
                    if (character.getHp() > 0) {
                        botAlive = true;
                        break;
                    }
                }
            }
        }

        if (playerAlive && !botAlive) {
            return true;
        } else if (!playerAlive && botAlive) {
            return true;
        }
        return false;
    }


    /**
     * Ezen metódus segítségével beállítható, hogy a játékot tutorial módban szeretnénk-e elindítani.<br>
     * Alapértelmezetten (ha nem mondunk semmit) nem tutorial módban indul el a játék.<br>
     * Tutorial módban a gépi karakterek nem végeznek cselekvést, csak egy helyben állnak.<br>
     * A tutorial mód beállítása még a karakterek csatlakozása előtt történik.
     *
     * @param tutorial igaz, amennyiben tutorial módot szeretnénk; hamis egyébként
     */
    public void setTutorial(boolean tutorial) {
        if (tutorial != this.tutorial) {
            this.tutorial = tutorial;
        }
    }
}
