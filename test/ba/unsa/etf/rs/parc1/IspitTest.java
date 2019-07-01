package ba.unsa.etf.rs.t5;

import static org.junit.jupiter.api.Assertions.*;

import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.api.FxRobot;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

@ExtendWith(ApplicationExtension.class)
class IspitTest {
    KorisniciModel model;
    @Start
    public void start (Stage stage) throws Exception {
        model = new KorisniciModel();
        model.napuni();
        KorisnikController ctrl = new KorisnikController(model);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/korisnici.fxml"));
        loader.setController(ctrl);
        Parent root = loader.load();
        stage.setTitle("Korisnici");
        stage.setScene(new Scene(root, 500, 275));
        stage.show();
        stage.toFront();
    }

    @Test
    void imaLiDugme(FxRobot robot) {
        Button btnObrisi = robot.lookup("#btnObrisi").queryAs(Button.class);
        assertNotNull(btnObrisi);
    }

    @Test
    void obrisiLista(FxRobot robot) {
        robot.clickOn("Sijerčić Tarik");
        robot.clickOn("#btnObrisi");
        ListView lista = robot.lookup("#listKorisnici").queryAs(ListView.class);
        ObservableList<Korisnik> korisniks = lista.getItems();
        String s = "";
        for(Korisnik k : korisniks)
            s += k;
        String expected = "Ljubović VedranDelić AmraFejzić Rijad";
        assertEquals(expected, s);
    }

    @Test
    void obrisiModel(FxRobot robot) {
        robot.clickOn("Delić Amra");
        robot.clickOn("#btnObrisi");
        ListView lista = robot.lookup("#listKorisnici").queryAs(ListView.class);
        ObservableList<Korisnik> korisniks = model.getKorisnici();
        String s = "";
        for(Korisnik k : korisniks)
            s += k;
        String expected = "Ljubović VedranSijerčić TarikFejzić Rijad";
        assertEquals(expected, s);
    }

    @Test
    void obrisiIzaberi(FxRobot robot) {
        robot.clickOn("Delić Amra");
        robot.clickOn("#btnObrisi");
        robot.clickOn("Fejzić Rijad");
        TextField polje = robot.lookup("#fldIme").queryAs(TextField.class);
        assertEquals("Rijad", polje.getText());
    }

    // Pomoćna funkcija za CSS
    boolean sadrziStil(TextField polje, String stil) {
        for (String s : polje.getStyleClass())
            if (s.equals(stil)) return true;
        return false;
    }

    @Test
    void validacijaPasswordPrazno(FxRobot robot) {
        robot.clickOn("Fejzić Rijad");
        robot.clickOn("#btnDodaj");
        TextField polje = robot.lookup("#fldPassword").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        assertFalse(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldPasswordRepeat").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        assertFalse(sadrziStil(polje, "poljeIspravno"));
    }

    @Test
    void validacijaPasswordDrugiPrazan(FxRobot robot) {
        robot.clickOn("Fejzić Rijad");
        robot.clickOn("#btnDodaj");
        robot.clickOn("#fldPassword");
        robot.write("abcde");
        TextField polje = robot.lookup("#fldPassword").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        assertFalse(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldPasswordRepeat").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        assertFalse(sadrziStil(polje, "poljeIspravno"));
    }

    @Test
    void validacijaPasswordJednaki(FxRobot robot) {
        robot.clickOn("Fejzić Rijad");
        robot.clickOn("#btnDodaj");
        robot.clickOn("#fldPassword");
        robot.write("abcde");

        robot.clickOn("#fldPasswordRepeat");
        robot.write("abcde");

        TextField polje = robot.lookup("#fldPassword").queryAs(TextField.class);
        assertFalse(sadrziStil(polje, "poljeNijeIspravno"));
        assertTrue(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldPasswordRepeat").queryAs(TextField.class);
        assertFalse(sadrziStil(polje, "poljeNijeIspravno"));
        assertTrue(sadrziStil(polje, "poljeIspravno"));
    }

    @Test
    void validacijaPasswordRazlike(FxRobot robot) {
        robot.clickOn("Fejzić Rijad");
        robot.clickOn("#btnDodaj");
        robot.clickOn("#fldPassword");
        robot.write("qwer");
        robot.clickOn("#fldPasswordRepeat");
        robot.write("qwe");
        TextField polje = robot.lookup("#fldPassword").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        assertFalse(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldPasswordRepeat").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        assertFalse(sadrziStil(polje, "poljeIspravno"));

        robot.write("r");

        polje = robot.lookup("#fldPassword").queryAs(TextField.class);
        assertFalse(sadrziStil(polje, "poljeNijeIspravno"));
        assertTrue(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldPasswordRepeat").queryAs(TextField.class);
        assertFalse(sadrziStil(polje, "poljeNijeIspravno"));
        assertTrue(sadrziStil(polje, "poljeIspravno"));

        robot.write("t");

        polje = robot.lookup("#fldPassword").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        assertFalse(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldPasswordRepeat").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        assertFalse(sadrziStil(polje, "poljeIspravno"));

        robot.clickOn("#fldPassword");
        robot.write("t");

        polje = robot.lookup("#fldPassword").queryAs(TextField.class);
        assertFalse(sadrziStil(polje, "poljeNijeIspravno"));
        assertTrue(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldPasswordRepeat").queryAs(TextField.class);
        assertFalse(sadrziStil(polje, "poljeNijeIspravno"));
        assertTrue(sadrziStil(polje, "poljeIspravno"));
    }
/*
    @Test
    void promjena(FxRobot robot) {
        robot.clickOn("Sijerčić Tarik");
        robot.clickOn("#fldIme").write("bbb");
        robot.clickOn("Fejzić Rijad");
        ListView lista = robot.lookup("#listKorisnici").queryAs(ListView.class);
        ObservableList<Korisnik> korisniks = lista.getItems();
        assertEquals("Sijerčić Tarikbbb", korisniks.get(2).toString());
    }

    @Test
    void promjenaModel(FxRobot robot) {
        robot.clickOn("Sijerčić Tarik");
        robot.clickOn("#fldIme").write("bbb");
        ListView lista = robot.lookup("#listKorisnici").queryAs(ListView.class);
        ObservableList<Korisnik> korisniks = model.getKorisnici();
        assertEquals("Sijerčić Tarikbbb", korisniks.get(2).toString());
    }

    @Test
    void dodavanje(FxRobot robot) {
        robot.clickOn("#btnDodaj");
        robot.clickOn("#fldIme").write("Ime");
        robot.clickOn("#fldPrezime").write("Prezime");
        robot.clickOn("#fldEmail").write("Email");
        robot.clickOn("#fldUsername").write("Username");
        robot.clickOn("#fldPassword").write("Password");
        ListView lista = robot.lookup("#listKorisnici").queryAs(ListView.class);
        ObservableList<Korisnik> korisniks = lista.getItems();
        assertEquals(5, korisniks.size());
        assertEquals("Prezime Ime", korisniks.get(4).toString());
        assertEquals("Email", korisniks.get(4).getEmail());
        assertEquals("Username", korisniks.get(4).getUsername());
        assertEquals("Password", korisniks.get(4).getPassword());
    }

    @Test
    void dodavanjeModel(FxRobot robot) {
        robot.clickOn("#btnDodaj");
        robot.clickOn("#fldIme").write("Ime");
        robot.clickOn("#fldPrezime").write("Prezime");
        robot.clickOn("#fldEmail").write("Email");
        robot.clickOn("#fldUsername").write("Username");
        robot.clickOn("#fldPassword").write("Password");
        ListView lista = robot.lookup("#listKorisnici").queryAs(ListView.class);
        ObservableList<Korisnik> korisniks = model.getKorisnici();
        assertEquals(5, korisniks.size());
        assertEquals("Prezime Ime", korisniks.get(4).toString());
        assertEquals("Email", korisniks.get(4).getEmail());
        assertEquals("Username", korisniks.get(4).getUsername());
        assertEquals("Password", korisniks.get(4).getPassword());
    }

    // Pomoćna funkcija za CSS
    boolean sadrziStil(TextField polje, String stil) {
        for (String s : polje.getStyleClass())
            if (s.equals(stil)) return true;
        return false;
    }


    @Test
    void validacija(FxRobot robot) {
        robot.clickOn("Delić Amra");
        robot.clickOn("#btnDodaj");

        TextField polje = robot.lookup("#fldIme").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        robot.clickOn("#fldIme").write("Ime");
        assertTrue(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldPrezime").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        robot.clickOn("#fldPrezime").write("Prezime");
        assertTrue(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldEmail").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        robot.clickOn("#fldEmail").write("Email");
        assertTrue(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldUsername").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        robot.clickOn("#fldUsername").write("Username");
        assertTrue(sadrziStil(polje, "poljeIspravno"));

        polje = robot.lookup("#fldPassword").queryAs(TextField.class);
        assertTrue(sadrziStil(polje, "poljeNijeIspravno"));
        robot.clickOn("#fldPassword").write("Password");
        assertTrue(sadrziStil(polje, "poljeIspravno"));
    }*/
}