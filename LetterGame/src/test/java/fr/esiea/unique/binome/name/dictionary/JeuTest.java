package fr.esiea.unique.binome.name.dictionary;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

/**
 * 
 * @author Florent
 *
 */

public class JeuTest {
	private Joueur j1 = new Joueur();
	private Joueur j2 = new Joueur();
	private Joueur ia = new Joueur();
	private Jeu jeu = new Jeu();
	private Dico dico = new Dico();

	@Test
	public void testInitPseudos() {
		j1.setPseudo("pseudo1");
		j2.setPseudo("pseudo1");
		assertNotEquals(j1.getPseudo(), null);
		assertNotEquals(j2.getPseudo(), null);
	}

	@Test
	public void testInitPseudo() {
		j1.setPseudo("pseudo1");
		ia.setPseudo("pseudoIA");
		assertNotEquals(j1.getPseudo(), null);
		assertNotEquals(ia.getPseudo(), null);
	}

	@Test
	public void testPioche() {
		jeu.pioche(j1);
		assertTrue(jeu.getLettreSurTable().size() >= 1);
	}

	@Test
	public void testFaitUnMot() {
		jeu.getLettreSurTable().add('t');
		jeu.getLettreSurTable().add('e');
		jeu.getLettreSurTable().add('s');
		jeu.getLettreSurTable().add('t');
		String mot = "test";
		if (jeu.motPossible(mot, j1)) {
			j1.setJoueurMots(mot);
			assertTrue(j1.getJoueurMots().size() == 1);
		} else {
			assertFalse(j1.getJoueurMots().size() == 1);
		}
	}

	@Test
	public void testMotPossible() {
		jeu.getLettreSurTable().add('t');
		jeu.getLettreSurTable().add('e');
		jeu.getLettreSurTable().add('s');
		jeu.getLettreSurTable().add('t');
		assertTrue(jeu.motPossible("test", j1));
	}

	@Test
	public void testFaitUnMotAvecUnMot() {
		jeu.getLettreSurTable().add('a');
		jeu.getLettreSurTable().add('b');
		jeu.getLettreSurTable().add('e');
		jeu.getLettreSurTable().add('r');
		j1.getJoueurMots().add("ration");
		for (int i = 0; i < j1.getJoueurMots().get(0).length(); i++) {
			jeu.getLettreSurTable().add(j1.getJoueurMots().get(0).charAt(i));
		}
		String mot = "aberration";
		if (jeu.motPossible(mot, j1)) {
			j1.getJoueurMots().remove("ration");
			j1.setJoueurMots(mot);
		}
		assertTrue(j1.getJoueurMots().get(0).equals("aberration"));
	}

	@Test
	public void testFaitUnMotAvecDeuxMot() {
		j1.setJoueurMots("aber");
		j2.setJoueurMots("ration");
		String mot = "";
		for (int i = 0; i < j1.getJoueurMots().size(); i++) {
			for (int j = 0; j < j2.getJoueurMots().size(); j++) {
				mot = j1.getJoueurMots().get(i).concat(j2.getJoueurMots().get(j));
				if (dico.motExiste(mot)) {
					j1.getJoueurMots().remove(j1.getJoueurMots().get(i));
					j2.getJoueurMots().remove(j2.getJoueurMots().get(j));
					j1.setJoueurMots(mot);
				}
			}
		}
		assertTrue(j1.getJoueurMots().get(0).equals(mot));
	}

	@Test
	public void testRemoveUnMot() {
		j1.getJoueurMots().add("test");
		jeu.removeUnMot(j1, j2, "test");
		assertTrue(j1.getJoueurMots().size() == 0);
	}

	@Test
	public void testTestGagnant() {
		for (int i = 0; i <= 9; i++) {
			j1.getJoueurMots().add("test" + i);
		}
		jeu.testGagnant(j1);
		assertTrue(j1.getGagnant() == true);
	}

	@Test
	public void motPossibleParIA() {
		jeu.getLettreSurTable().add('m');
		jeu.getLettreSurTable().add('a');
		jeu.getLettreSurTable().add('m');
		jeu.getLettreSurTable().add('a');
		jeu.getLettreSurTable().add('n');
		assertTrue(jeu.motPossibleParIA().contains("maman"));
	}

	@Test
	public void testFaitUnMotIA() {
		List<String> listeDeMots = new ArrayList<String>();
		String motAFaire = "";
		listeDeMots.add("man");
		listeDeMots.add("manan");
		listeDeMots.add("tresLongMot");
		for (int i = 0; i < listeDeMots.size(); i++) {
			if (jeu.estLeMeilleurMot(listeDeMots.get(i), motAFaire)) {
				motAFaire = listeDeMots.get(i);
			}
		}
		assertTrue(motAFaire.equals("tresLongMot"));
	}

	@Test
	public void testEstLeMeilleurMot() {
		String mot = "manan";
		String meilleurMot = "tresLongMot";
		assertTrue(jeu.estLeMeilleurMot(meilleurMot, mot));
	}

	@Test
	public void testMotAvecUnMotIA() {
		j1.setJoueurMots("man");
		j1.setJoueurMots("test");
		jeu.getLettreSurTable().add('a');
		jeu.getLettreSurTable().add('m');
		for (int i = 0; i < j1.getJoueurMots().size(); i++) {
			String oldMot = j1.getJoueurMots().get(i);
			for(int j = 0; j < oldMot.length(); j++) {
				jeu.getLettreSurTable().add(oldMot.charAt(j));
			}
			List<String> newMots = jeu.motPossibleParIA();
			for(int k = 0; k < jeu.motPossibleParIA().size(); k++) {
				String newMot = newMots.get(k);
				if(newMot.contains(oldMot) && !newMot.equals(oldMot)) {
					j1.getJoueurMots().remove(oldMot);
					j1.getJoueurMots().add(newMot);
					for(int j = 0; j < newMot.length(); j++) {
						jeu.getLettreSurTable().remove((Character) newMot.charAt(j));
					}
				}
			}
		}
		assertTrue(j1.getJoueurMots().size() == 2);
		assertTrue(j1.getJoueurMots().contains("maman"));
	}

	@Test
	public void testMotAvecDeuxMotsIA() {
		j1.setJoueurMots("aber");
		j2.setJoueurMots("ration");
		String mot = "";
		for (int i = 0; i < j1.getJoueurMots().size(); i++) {
			for (int j = 0; j < j2.getJoueurMots().size(); j++) {
				mot = j1.getJoueurMots().get(i).concat(j2.getJoueurMots().get(j));
				if (dico.motExiste(mot)) {
					j1.getJoueurMots().remove(j1.getJoueurMots().get(i));
					j2.getJoueurMots().remove(j2.getJoueurMots().get(j));
					j1.setJoueurMots(mot);
				}
			}
		}
		assertTrue(j1.getJoueurMots().get(0).equals(mot));
	}

	/*
	 * Les autres méthodes méthodes peuvent être testées en lançant le jeu avec
	 * la console
	 */
}
