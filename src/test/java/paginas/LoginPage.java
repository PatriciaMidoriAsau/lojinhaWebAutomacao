package paginas; //     Design pattern page object

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.List;

public class LoginPage {
    private WebDriver navegador;     //atributo privado que representa seu Driver

    public LoginPage(WebDriver navegador){     //  construtor que recebe o estado atual do seu navegador e passa para essa classe
        this.navegador = navegador;
    }

    public LoginPage informarOUsuario(String usuario) {
        navegador.findElement(By.cssSelector("label[for='usuario']")).click();
        navegador.findElement(By.id("usuario")).sendKeys(usuario);

        return this;     //Design pattern fluent page
    }

    public LoginPage informarASenha(String senha) {
        navegador.findElement(By.cssSelector("label[for='senha']")).click();
        navegador.findElement(By.id("senha")).sendKeys(senha);

        return this;
    }

//          \/ nome do método será o da próxima página após realizar a ação, ao enviar o formulário
//             será carregada a tela de produtos. ( A classe ListaDeProdutosPage já foi criada em paginas)
    public ListaDeProdutosPage submeterFormularioDeLogin() {
        navegador.findElement(By.cssSelector("button[type='submit']")).click();

        return new ListaDeProdutosPage(navegador);
    }
}









