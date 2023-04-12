package com.scrapperescapetime;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableCellElement;
import com.gargoylesoftware.htmlunit.javascript.host.html.HTMLTableDataCellElement;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gson.*;

public class ClientGM extends WebClient {
    private final String identifant;
    private final String motDePasse;
    private final String ville;
	private URL jourActuel;
    private URL urldeConnexion;
	private URL jourSuivant;
	private URL jourPrecedent;
	private String url = "https://gmet.escapetime-world.fr/repartition.php";

    ClientGM(String login, String mdp, String ville){
        super(BrowserVersion.BEST_SUPPORTED);
        this.identifant = login;
        this.motDePasse = mdp;
        this.ville = ville; 
		try {this.jourPrecedent = new URL("https://gmet.escapetime-world.fr/repartition.php?jour_change=precedent"); } catch (Exception e) { System.out.println("c la merde"); System.exit(0); }
		try {this.jourActuel = new URL("https://gmet.escapetime-world.fr/repartition.php?jour_change=actu"); } catch (Exception e) { System.out.println("c la merde"); System.exit(0);}
		try {this.jourSuivant = new URL("https://gmet.escapetime-world.fr/repartition.php?jour_change=suivant"); } catch (Exception e) { System.out.println("c la merde"); System.exit(0); }
        try {this.urldeConnexion = new URL("https://gmet.escapetime-world.fr/index.php?auth=" + ville); } catch (Exception e) { System.out.println("c la merde"); System.exit(0); }
        //this.getCookieManager().setCookiesEnabled(true);
        //this.getOptions().setJavaScriptEnabled(true);
        this.getOptions().setCssEnabled(false);
        
        System.out.println("Construction du client GM avec pour url de connexion:\n" + this.urldeConnexion + "\n");
    }

    public String getIdentifant() {
        return identifant;
    }

    public String getVille() {
        return ville;
    }

	private void changerJourSecret(URL choix){
		try {
			this.getPage(choix);
		} catch (Exception e) {
			System.out.println("c la merde");
			System.exit(0);
		}
		System.out.println("Changement de jour");
	}
	public void changerJourSuivant() {
		changerJourSecret(this.jourSuivant);
	}

	public void changerJourPrecedent() {
		changerJourSecret(this.jourPrecedent);
	}

	public void changerJourActuel(){
		changerJourSecret(this.jourActuel);
	}

    public Boolean connecter() {
        System.out.println("Connexion au site de " + this.ville + "\n");
        try {
			
            HtmlPage loginPage = this.getPage(urldeConnexion);
            
            
			
			//Create an HtmlForm by locating the form that pertains to logging in.
			//"//form[@id='login-form']" means "Hey, look for a <form> tag with the
			//id attribute 'login-form'" Sound familiar?
			//<form id="login-form" method="post" ...
			HtmlForm loginFormulaire = loginPage.getFirstByXPath("//form[@name='auth_valid']");

			//This is where we modify the form. The getInputByName method looks
			//for an <input> tag with some name attribute. For example, user or passwd.
			//If we take a look at the form, it all makes sense.
			//<input value="" name="user" id="user_login" ...
            
			//After we locate the input tag, we set the value to what belongs.
			//So we're saying, "Find the <input> tags with the names "user" and "passwd"
			//and throw in our username and password in the text fields.
			loginFormulaire.getInputByName("ident").setValueAttribute(identifant);
			loginFormulaire.getInputByName("code").setValueAttribute(motDePasse);
			
			//<button type="submit" class="c-btn c-btn-primary c-pull-right" ...
			//Okay, you may have noticed the button has no name. What the line
			//below does is locate all of the <button>s in the login form and
			//clicks the first and only one. (.get(0)) This is something that
			//you can do if you come across inputs without names, ids, etc.

            //On recupere l'element bouton via son id
            loginPage.getHtmlElementById("creer").click();
			
		} catch (FailingHttpStatusCodeException e) {
			System.out.println("code d'erreur HTTP: " + e.getStatusCode() + "\n\n\n");
		} catch (MalformedURLException e) {
			System.out.println("URL mal formée: " + e.getMessage() + "\n\n\n");
		} catch (IOException e) {
			System.out.println("Erreur d'entrée/sortie: " + e.getMessage() + "\n\n\n");
		}
        return true;
    }

    public ArrayList<String> getNomSalle() {
        try {
			//All this method does is return the HTML response for some URL.
			//We'll call this after we log in!
			ArrayList<String> nomSalle = new ArrayList<String>();
			HtmlPage page = this.getPage(this.url);
			List<HtmlElement> divs = page.getByXPath("/html/body/center/div[1]/table[1]/tbody/tr[1]/td");
			for (HtmlElement cell: divs){
				if(!cell.getTextContent().contains("Heure") && !cell.getTextContent().contains("Accueil"))
				nomSalle.add(cell.getTextContent());
			}
			divs = page.getByXPath("/html/body/center/div[1]/table[2]/tbody/tr[1]/td[2]");
			for (HtmlElement cell: divs){
				if(!cell.getTextContent().contains("Heure") && !cell.getTextContent().contains("Accueil"))
				nomSalle.add(cell.getTextContent());
			}
			
			return nomSalle;
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
    }

	public Map<String, String> getClientParSalle(Salle choix){
		
		Integer salle = choix.getVal() * 2 + 2;

		try {
			//All this method does is return the HTML response for some URL.
			//We'll call this after we log in!
			Map<String, String> clientParSalle = new HashMap<>();
			HtmlPage page = this.getPage(this.url);
			for(int i=2; i < 10; i++){
				//String t = page.getFirstByXPath("/html/body/center/div[1]/table[1]/tbody/tr["+i+"]/td[4]");
				List<HtmlElement> clients = page.getByXPath("/html/body/center/div[1]/table[1]/tbody/tr["+i+"]/td["+salle+"]");
				List<HtmlElement> heures = page.getByXPath("/html/body/center/div[1]/table[1]/tbody/tr["+i+"]/td[1]");
				for (int j=0; j < clients.size(); j++){
					if(!clients.get(j).getTextContent().isEmpty())
						clientParSalle.put(heures.get(j).getTextContent(), clients.get(j).getTextContent());
				}
			}
			return clientParSalle;
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}



	public JsonObject getClientParSalle(Salle choix, Integer nbJour){
		Integer salle = choix.getVal() * 2 + 2;
		String nomSalle = choix.getNom();
		JsonObject result = new JsonObject();
		try {
			changerJourActuel();
			for(int jour = 0; jour<nbJour; jour++){
				JsonObject jourObj = new JsonObject();
				HtmlPage page = this.getPage(this.url);
				for(int i=2; i < 10; i++){
					List<HtmlElement> clients = page.getByXPath("/html/body/center/div[1]/table[1]/tbody/tr["+i+"]/td["+salle+"]");
					List<HtmlElement> gm = page.getByXPath("/html/body/center/div[1]/table[1]/tbody/tr["+i+"]/td["+(salle-1)+"]");
					List<HtmlElement> heures = page.getByXPath("/html/body/center/div[1]/table[1]/tbody/tr["+i+"]/td[1]");
					for (int j=0; j < clients.size(); j++){
						if(!clients.get(j).getTextContent().isEmpty()) {
							JsonObject clientObj = new JsonObject();
							clientObj.addProperty("gm", gm.get(j).getTextContent());
							clientObj.addProperty("client", clients.get(j).getTextContent());
							jourObj.add(heures.get(j).getTextContent(), clientObj);
						}
					}
				}
				result.add("jour"+(jour+1), jourObj);
				changerJourSuivant();
			}

			return result;
		} catch (FailingHttpStatusCodeException e) {
			e.printStackTrace();
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
}
