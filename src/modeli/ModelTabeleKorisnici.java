/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeli;

import domen.Korisnik;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.table.AbstractTableModel;
import konstante.Operacije;
import kontroler.Komunikacija;
import transfer.KlijentskiZahtev;
import transfer.ServerskiOdgovor;

/**
 *
 * @author frado
 */
public class ModelTabeleKorisnici extends AbstractTableModel implements Runnable{
    
    ArrayList<Korisnik> lista;
    String[] kolone = {"KorisnikID", "Username"};

    public ModelTabeleKorisnici() {
        
        KlijentskiZahtev kz = new KlijentskiZahtev();
        kz.setOperacije(Operacije.VRATI_ULOGOVANE);
        
        Komunikacija.getInstance().posaljiZahtev(kz);
        ServerskiOdgovor so = Komunikacija.getInstance().primiOdgovor();
        
        lista = (ArrayList<Korisnik>) so.getOdgovor();
        
    }
    
    

    @Override
    public int getRowCount() {
        return lista.size();
    }

    @Override
    public int getColumnCount() {
        return kolone.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolone[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        Korisnik k = lista.get(rowIndex);
        
        switch(columnIndex){
            case 0:
                return k.getKorisnikID();
            case 1:
                return k.getUsername();
                
            default:
                return "return!";
        }
    }

    @Override
    public void run() {
        while (true) {            
            try {
                KlijentskiZahtev kz = new KlijentskiZahtev();
                kz.setOperacije(Operacije.VRATI_ULOGOVANE);
                Komunikacija.getInstance().posaljiZahtev(kz);
                ServerskiOdgovor so = Komunikacija.getInstance().primiOdgovor();
                lista = (ArrayList<Korisnik>) so.getOdgovor();
                fireTableDataChanged();
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ModelTabeleKorisnici.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public Korisnik getKorisnik(int row) {
        return lista.get(row);
    }
    
    
    
}
