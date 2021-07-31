import caseClasses.Admin
import slick.jdbc.H2Profile.api._

import java.time.LocalDateTime
import scala.swing._

class FenAjoutAdmin extends Frame{
  title = "Ajouter un nouveau administrateur"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  var textFieldPrenom = new TextField()
  var textFieldNom = new TextField()
  var textFieldlogin = new TextField()
  var textFieldPasse = new PasswordField()
  var labelPrenom = new Label("Prénom : ")
  var labelNom = new Label("Nom : ")
  var labelLogin = new Label("Login : ")
  var labelPasse = new Label("Mot de passe : ")

  val panelNom = new BoxPanel(Orientation.Horizontal){
    contents += labelNom
    contents += Swing.HStrut(18)
    contents += textFieldNom
  }
  val panelPrenom = new BoxPanel(Orientation.Horizontal){
    contents += labelPrenom
    contents += Swing.HStrut(2)
    contents += textFieldPrenom
  }
  val panelGrade = new BoxPanel(Orientation.Horizontal){
    contents += labelLogin
    contents += Swing.HStrut(12)
    contents += textFieldlogin
  }
  val panelMatiere = new BoxPanel(Orientation.Horizontal){
    contents += labelPasse
    contents += textFieldPasse
  }

  contents = new BoxPanel(Orientation.Vertical){
    contents += panelNom
    contents += Swing.VStrut(5)
    contents += panelPrenom
    contents += Swing.VStrut(5)
    contents += panelGrade
    contents += Swing.VStrut(5)
    contents += panelMatiere
    contents += Swing.VStrut(5)
    contents += new Button("enregistrer"){
      listenTo(mouse.clicks)
      reactions += {
        case _: event.MouseClicked => {
          if(textFieldNom.text.isEmpty | textFieldPrenom.text.isEmpty | textFieldlogin.text.isEmpty){
            Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title="Message d'erreur")
          }
          else {
            acces.ajouterAdmin(Admin(1, textFieldlogin.text, textFieldPasse.text, textFieldNom.text,
              textFieldPrenom.text, LocalDateTime.now().toString, ""))
            Dialog.showMessage(contents.head, "Enregistrement effectué avec succès !", title="Message de confirmation")
          }
        }
      }
    }
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }
  centerOnScreen()
  size = new Dimension(350, 250)

}
