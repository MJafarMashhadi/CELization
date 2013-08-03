/**
 * 
 */
package celization;

/**
 * @author mjafar
 * 
 */
public final class NameChooser {
	public static final String[] names = ("Jafar;Esckatch;Milano;Niloofar;Foroozan;"
			+ "Pashang;Sadra;Gaga;Shakira;Sadr;Gerdoo;Grace;Kelly;Ringo;Niusha;"
			+ "Homeyra;Jasem;Abdollah;Mehrzad;Dorara;Uma;Jessica;Me-Gusta;Fariba;"
			+ "Homer;Hoodad;Hooyar;Marge;Maggie;Lisa;Bart;Gandolf;Azade;Shabnam;"
			+ "Montgomery-Burns;Waylon;Lenny;Stewie;Santa's-little-helper;Penny;"
			+ "Forever-Alone;Borzooye;Ermia;Elena;Elika;Tara;Shirin;Julian;Jedi;"
			+ "Omaar;Heisenberg;Jenna;Natasha;Sasha;Alexandra;Adriana;Megan;Cat;"
			+ "Chester;Roger-Waters;Deziree;Ozhny;Johan;Jacob;Holden;Anna;Hanna;"
			+ "Zolfaghar;Goofy;Yoogi;Big-Bear;Pumba;Pooh;Pou;Lebowski;Navid;Cheryl;"
			+ "Javid;Lionel;Michael;Oscar;Shahshah;Miran;Shahan;Janyar;Janan;Marlon;"
			+ "Adolph;Van-Persie;McDonald;Mashti;Arshia;Hadi;Nima;Yasin;Asghar;"
			+ "Leatherhead;Steven;Alan-Wake;Beigi;Arian;Forough;Alireza;Vincent-Vega;"
			+ "Speed;Bernard;Bernadet;Clarry;Hillary;Barack;Mahmood;Ahmad;Quimby;"
			+ "George;Benjamin;Cayotte;Tom;Jerry;Hugo;Pat;Mat;Selma;Patty;Milo;"
			+ "Mahdyar;Shayan;Angel;Chris;Hoomaan;Hooman;Shaun;Bell;Sebastian;Mario;"
			+ "Avril;Luke;Johanna;Jolie;Roksana;Matthew;Rastapopoulos;Hadook;Gimp;"
			+ "Zed;Forrest;L;Kira;Neo;Trinity;Morphious;Rainmaker;Chandler;Eddie;"
			+ "Leon;Enrique;Corey;Al;Pingu;Haji;Khosi;A'min;Daisy;Corleone;Quvenzhane;"
			+ "Jules;Mia;Marcellos;Butch;Esmeralda;Winston;Quentin;Bill;Django;"
			+ "Lincoln;Seth;Matt-Groening;Dexter;Rita;Doakes;Debra;Harry;Clint;"
			+ "Hoo-Joo;Kim-Lee;Chuck-Norris;Pikachu;Wolf;Ace;Dante;Vergil;Daryl;"
			+ "Ghazal;Mullengweg;Stallman;Torvalds;Sherlock;Taylor;Francis;Eva;"
			+ "Katy;Pandoo;Subasa;Kakero;Jesus;Adam;Eve;Snowball-I;Snowball-II;"
			+ "Luigi;Mana;Mona;Kiana;Farnaz;Keyvan;Gil;Karen;Nadia;Yasir;Wayne").split(";");

	public static int[] namesUsage = new int[names.length];
	public static int namesUsed = 0;

	public static String getFreeName() {
		int index;
		boolean repetitive;

		String chosenName;

		do {
			index = CElization.rndMaker.nextInt(names.length);
			chosenName = names[index];
			repetitive = false;

			if (namesUsage[index] != 0) {
				/** pigeon hole principle */
				if (namesUsed > names.length) {
					/** Add "-(3)" to the end of the name */
					chosenName = String.format("%s-(%d)", chosenName,
							namesUsage[index]);

					repetitive = false;
					break;
				} else {
					/** choose another name */
					repetitive = true;
					break;
				}
			}
		} while (repetitive);

		namesUsage[index]++;
		namesUsed++;

		return chosenName;
	}
}
