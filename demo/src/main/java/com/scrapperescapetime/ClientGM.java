package com.scrapperescapetime;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.*;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

public class ClientGM extends WebClient {
    private final String identifant;
    private final String motDePasse;
    private final String ville;
    private URL urldeConnexion;

    ClientGM(String login, String mdp, String ville){
        super(BrowserVersion.BEST_SUPPORTED);
        this.identifant = login;
        this.motDePasse = mdp;
        this.ville = ville;

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

    public String getNomSalle(String url) {
        try {
			//All this method does is return the HTML response for some URL.
			//We'll call this after we log in!
			return this.getPage(url).getWebResponse().getContentAsString();
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
