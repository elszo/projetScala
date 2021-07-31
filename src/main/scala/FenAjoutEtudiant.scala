import caseClasses.{Etudiant, EtudiantMatiere}
import cats.implicits.catsSyntaxEq
import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.swing._

class FenAjoutEtudiant extends Frame{
  title = "Ajouter un nouveau professeur"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  var textFieldPrenom = new TextField()
  var textFieldNom = new TextField()
  var comboBoxMatiere = new ComboBox(List("","6ième","5ième","4ième","3ième"))
  var labelPrenom = new Label("Prénom : ")
  var labelNom = new Label("Nom : ")
  var labelMatiere = new Label("Classe : ")

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

  val panelMatiere = new BoxPanel(Orientation.Horizontal){
    contents += labelMatiere
    contents += Swing.HStrut(5)
    contents += comboBoxMatiere
  }

  contents = new BoxPanel(Orientation.Vertical){
    contents += panelNom
    contents += Swing.VStrut(5)
    contents += panelPrenom
    contents += Swing.VStrut(5)
    contents += panelMatiere
    contents += Swing.VStrut(5)
    contents += new Button("enregistrer"){
      listenTo(mouse.clicks)
      reactions += {
        case _: event.MouseClicked => {
          if(textFieldNom.text.isEmpty | textFieldPrenom.text.isEmpty |
            comboBoxMatiere.selection.item.isEmpty){
            Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title="Message d'erreur")
          }
          else {
            acces.ajouterEtudiant(Etudiant(1, textFieldNom.text, textFieldPrenom.text,
              comboBoxMatiere.selection.item))
            val etud = Await.result(acces.tousEtudiants, Duration.Inf)
            val et = etud.filter(_.nomEtud===textFieldNom.text).filter(_.prenomEtud===textFieldPrenom.text).map(_.idEtud)
            val mat = Await.result(acces.toutesMatieres, Duration.Inf)
            for (m <- mat) {
              acces.ajouterEtudiantMatiere(EtudiantMatiere(et(0), m.codeMatiere, 0.0, 0.0, 0.0))
            }
            Dialog.showMessage(contents.head, "Enregistrement effectué avec succès !", title="Message de confirmation")
          }
        }
      }
    }
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }
  centerOnScreen()
  size = new Dimension(350, 220)

}
