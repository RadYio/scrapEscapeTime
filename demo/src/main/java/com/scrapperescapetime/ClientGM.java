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
import java.util.List;

public class ClientGM extends WebClient {
    private final String identifant;
    private final String motDePasse;
    private final String ville;
    private URL urldeConnexion;
	private URL jourSuivant;
	private URL jourPrecedent;

    ClientGM(String login, String mdp, String ville){
        super(BrowserVersion.BEST_SUPPORTED);
        this.identifant = login;
        this.motDePasse = mdp;
        this.ville = ville; 
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

    public ArrayList<String> getNomSalle(String url) {
        try {
			//All this method does is return the HTML response for some URL.
			//We'll call this after we log in!
			ArrayList<String> nomSalle = new ArrayList<String>();
			HtmlPage page = this.getPage(url);
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
}
