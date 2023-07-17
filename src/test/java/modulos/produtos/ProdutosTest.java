package modulos.produtos;

import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import paginas.LoginPage;


import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.Duration;
import java.util.Properties;

@DisplayName("Testes Web do módulo de Produtos")
public class ProdutosTest {

    private WebDriver navegador; //atributo que estava dentro do beforeEach, colocamos no
                                // escopo da classe para que fique visível em todos os métodos.
    private String username;
    private String password;
    private String baseUrl;

    {
        try (InputStream input = new FileInputStream("E:\\Paty\\projetosQaPastaCerta\\lojinhaWebAutomacao\\config.properties")) {

            Properties prop = new Properties();
            prop.load(input);
            this.baseUrl = prop.getProperty("db.url");
            this.username = prop.getProperty("db.user");
            this.password = prop.getProperty("db.password");

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @BeforeEach
    public void beforeEach(){
        // Abrir o navegador
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        DesiredCapabilities cp = new DesiredCapabilities();
        cp.setCapability(ChromeOptions.CAPABILITY, options);
        options.merge(cp);

        System.setProperty("webdriver.chrome.driver", "C:\\Drivers\\chromedriver_win32\\chromedriver.exe");
        //"webdriver.chrome.driver" essa chave veio da documentação do webdriver para que o sistema
        // saiba qual drive será utilizado do chrome
        // "C:\Drivers\chromedriver_win32\chromedriver.exe" endereço que vai executar o driver, caso
        // versão do Chrome atualize, precisa baixar o novo driver e trocar aqui

        this.navegador = new ChromeDriver(options);
        //  /\ biblioteca    /\ variável     /\ driver utilizado

        // Vou maximizar a tela
        this.navegador.manage().window().maximize();

        // Vou definir um tempo de espera padrão de 5 segundos
        this.navegador.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        // Navegar para a página da lojinha web
        this.navegador.get(baseUrl);

    }

    @Test
    @DisplayName("Não é permitido registrar um produto com valor igual a zero")
    public void testNaoEPermitidoRegistrarProdutoComValorIgualAZero(){

        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario(username)
                .informarASenha(password)
                .submeterFormularioDeLogin()
                .acessarFormularioAdicaoNovoProduto()
                .informarNomeDoProduto("Mackbook Pro")
                .informarValorDoProduto("000")
                .informarCoresDoProduto("preto, branco")
                .submeterFormularioDeAdicaoComErro()
                .capturarMensagemApresentada();

        Assertions.assertEquals("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00", mensagemApresentada);

    }

    @Test
    @DisplayName("Não é permitido registrar um produto com valor maior que R$7.000,00")
    public void testNaoEPermitidoRegistrarProdutoComValorAcimaDeSeteMil(){
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario(username)
                .informarASenha(password)
                .submeterFormularioDeLogin()
                .acessarFormularioAdicaoNovoProduto()
                .informarNomeDoProduto("Macbook Pro")
                .informarValorDoProduto("700001")
                .informarCoresDoProduto("preto, branco")
                .submeterFormularioDeAdicaoComErro()
                .capturarMensagemApresentada();

        Assertions.assertEquals("O valor do produto deve estar entre R$ 0,01 e R$ 7.000,00", mensagemApresentada);
    }

    @Test
    @DisplayName("Posso adicionar produtos que estejam no limite de 0,01")
    public void testPossoAdicionarProdutosComValorDeUmCentavo(){
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario(username)
                .informarASenha(password)
                .submeterFormularioDeLogin()
                .acessarFormularioAdicaoNovoProduto()
                .informarNomeDoProduto("Macbook Pro")
                .informarValorDoProduto("001")
                .informarCoresDoProduto("preto")
                .submeterFormularioDeAdicaoComSucesso()
                .capturarMensagemApresentada();

        Assertions.assertEquals("Produto adicionado com sucesso", mensagemApresentada);
    }

    @Test
    @DisplayName("Posso adicionar produtos que estejam no limite de 7.000,00")
    public void testPossoAdicionarProdutosComValorDeSeteMilReais(){
        String mensagemApresentada = new LoginPage(navegador)
                .informarOUsuario(username)
                .informarASenha(password)
                .submeterFormularioDeLogin()
                .acessarFormularioAdicaoNovoProduto()
                .informarNomeDoProduto("Macbook Pro")
                .informarValorDoProduto("700000")
                .informarCoresDoProduto("preto")
                .submeterFormularioDeAdicaoComSucesso()
                .capturarMensagemApresentada();

        Assertions.assertEquals("Produto adicionado com sucesso", mensagemApresentada);
    }

    @AfterEach
    public void afterEach(){
        // fecha o navegador ao final do teste:
         navegador.quit();
    }
}
