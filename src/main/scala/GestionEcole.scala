import cats.implicits.catsSyntaxEq
import slick.jdbc.H2Profile.api._
import scala.concurrent.ExecutionContext.Implicits.global
import javax.swing.ImageIcon
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.swing.Font.SansSerif
import scala.swing.Font.Style.BoldItalic
import scala.swing._

object GestionEcole extends SimpleSwingApplication with SchemaBD with InitialData with Magie {
  val db = Database.forConfig("h2")

  def top = new Frame {
    title = "Gestion de l'établissement"
    val labelAuth = new Label("Authentification")
    labelAuth.font = Font(SansSerif, BoldItalic, 30)
    labelAuth.foreground = new Color(30,144,255)
    val labelIcon = new Label()
    labelIcon.icon = new ImageIcon(getClass.getResource("images/connexion.png"))
    val loginTextField = new TextField()
    val passwordTextField = new PasswordField()
    //labelIcon.preferredSize = new Dimension(100, 100)
    contents = new BoxPanel(Orientation.Vertical) {
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += labelIcon
        contents += Swing.HStrut(5)
        contents += labelAuth
      }
      contents += Swing.VStrut(10)
      contents += Swing.Glue
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("Login : ")
        contents += Swing.HStrut(5)
        contents += loginTextField
      }
      contents += Swing.VStrut(5)
      contents += new BoxPanel(Orientation.Horizontal) {
        contents += new Label("Mot de passe : ")
        contents += Swing.HStrut(5)
        contents += passwordTextField
      }
      contents += Swing.VStrut(10)
      contents += Button("se connecter") {
        val admins = Await.result(acces.tousAdmins, Duration.Inf)
        val admin = for {
          ad <- admins if ad.login === loginTextField.text
        }yield ad.login
        if (admin.isEmpty) { Dialog.showMessage(contents.head, "Login ou Mot de passe incorrect !", title="Avertissement !") }
        else {
          def fen = new FenetrePrincipale
          fen.open()
          close()
          enabled = false
        }
      }
      //contents += Button("Close") { sys.exit(0) }
      border = Swing.EmptyBorder(10, 10, 10, 10)

      private val future = createSchemaIfNotExists.flatMap(_ => insertInitialData())
      Await.ready(future, Duration.Inf)

      val acces = new AccesDonnees(db)
    }
    this.size = new Dimension(380, 270)
    centerOnScreen()
  }
}
