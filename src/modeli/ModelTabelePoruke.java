/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package modeli;

import domen.Korisnik;
import domen.Poruka;
import java.text.SimpleDateFormat;
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
public class ModelTabelePoruke extends AbstractTableModel implements Runnable{
    
    ArrayList<Poruka> lista;
    String[] kolone = {"Datum i vreme", "Posiljalac", "Primalac", "Poruka"};
    Korisnik ulogovani;

    public ModelTabelePoruke(Korisnik ulogovani) {
        //vracamo poruke za ulogovanog
        this.ulogovani = ulogovani;
        
        KlijentskiZahtev kz = new KlijentskiZahtev();
        kz.setOperacije(Operacije.VRATI_PORUKE);
        kz.setParametar(ulogovani);
        
        Komunikacija.getInstance().posaljiZahtev(kz);
        ServerskiOdgovor so = Komunikacija.getInstance().primiOdgovor();
        
        lista = (ArrayList<Poruka>) so.getOdgovor();
        
        //vraacamo poslednje tri poruke
        ArrayList<Poruka> poslednjeTri = new ArrayList<>();
        
        poslednjeTri.add(lista.get(lista.size() - 1));
        poslednjeTri.add(lista.get(lista.size() - 2));
        poslednjeTri.add(lista.get(lista.size() - 3));
        
        Komunikacija.getInstance().getKf().setujPoslednjeTri(poslednjeTri);
        
    }

    public ModelTabelePoruke() {
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
        Poruka p = lista.get(rowIndex);
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH.mm");
        
        switch(columnIndex){
            case 0:
                return sdf.format(p.getDatumVreme());
            case 1:
                return p.getPosiljalac();
            case 2:
                return p.getPrimalac();
            case 3:
                if (p.getPoruka().length() > 20) {
                    return p.getPoruka().substring(0, 20) + " (20)";
                }
                return p.getPoruka();
                
            default: return "return!";
        }
    }

    @Override
    public void run() {
        while (true) {            
            try {
                this.ulogovani = ulogovani;
                
                KlijentskiZahtev kz = new KlijentskiZahtev();
                kz.setOperacije(Operacije.VRATI_PORUKE);
                kz.setParametar(ulogovani);
                
                Komunikacija.getInstance().posaljiZahtev(kz);
                ServerskiOdgovor so = Komunikacija.getInstance().primiOdgovor();
                
                lista = (ArrayList<Poruka>) so.getOdgovor();
                
                ArrayList<Poruka> poslednjeTri = new ArrayList<>();
                
                poslednjeTri.add(lista.get(lista.size() - 1));
                poslednjeTri.add(lista.get(lista.size() - 2));
                poslednjeTri.add(lista.get(lista.size() - 3));
                
                Komunikacija.getInstance().getKf().setujPoslednjeTri(poslednjeTri);
                Thread.sleep(3000);
            } catch (InterruptedException ex) {
                Logger.getLogger(ModelTabelePoruke.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    public void dodajPoslednjeTri(ArrayList<Poruka> poslednjeTri) {
        lista = poslednjeTri;
        fireTableDataChanged();
    }

    public String vratiPoruku(int row) {
        return lista.get(row).getPoruka();
    }
    
    
    
}
