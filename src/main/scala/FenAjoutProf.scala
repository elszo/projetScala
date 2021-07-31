import caseClasses.Professeur

import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.swing._
import slick.jdbc.H2Profile.api._

class FenAjoutProf extends Frame{
  title = "Ajouter un nouveau professeur"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)
  var  listeMatiere = ListBuffer[String]()
  listeMatiere += ""
  val matiers = Await.result(acces.toutesMatieres, Duration.Inf)
  for(m <- matiers){
    listeMatiere += m.codeMatiere
  }

  var textFieldPrenom = new TextField()
  var textFieldNom = new TextField()
  var textFieldGrade = new TextField()
  var comboBoxMatiere = new ComboBox(listeMatiere)
  var labelPrenom = new Label("Prénom : ")
  var labelNom = new Label("Nom : ")
  var labelGrade = new Label("Grade : ")
  var labelMatiere = new Label("Matière : ")

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
    contents += labelGrade
    contents += Swing.HStrut(12)
    contents += textFieldGrade
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
    contents += panelGrade
    contents += Swing.VStrut(5)
    contents += panelMatiere
    contents += Swing.VStrut(5)
    contents += new Button("enregistrer"){
      listenTo(mouse.clicks)
      reactions += {
        case _: event.MouseClicked => {
          if(textFieldNom.text.isEmpty | textFieldPrenom.text.isEmpty |
            textFieldGrade.text.isEmpty | comboBoxMatiere.selection.item.isEmpty){
            Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title="Message d'erreur")
          }
          else {
            acces.ajouterProfesseur(Professeur(1, textFieldNom.text, textFieldPrenom.text,
              textFieldGrade.text, comboBoxMatiere.selection.item))
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
