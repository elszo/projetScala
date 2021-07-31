import caseClasses.Salle
import slick.jdbc.H2Profile.api._

import scala.swing._

class FenAjoutSalle extends Frame{
  title = "Ajouter une nouvelle salle"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  var textFieldNumero = new TextField()
  var textFieldLibelle = new TextField()
  var labelNumero = new Label("Numéro : ")
  var labelLibelle = new Label("Libellé : ")

  val panelNumero = new BoxPanel(Orientation.Horizontal){
    contents += labelNumero
    contents += Swing.HStrut(10)
    contents += textFieldNumero
  }
  val panelLibelle = new BoxPanel(Orientation.Horizontal){
    contents += labelLibelle
    contents += Swing.HStrut(2)
    contents += textFieldLibelle
  }

  contents = new BoxPanel(Orientation.Vertical){
    contents += panelNumero
    contents += Swing.VStrut(5)
    contents += panelLibelle
    contents += Swing.VStrut(5)
    contents += new Button("enregistrer"){
      listenTo(mouse.clicks)
      reactions += {
        case _: event.MouseClicked => {
          if(textFieldNumero.text.isEmpty | textFieldLibelle.text.isEmpty){
            Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title="Message d'erreur")
          }
          else {

            acces.ajouterSalle(Salle(textFieldNumero.text.toInt, textFieldLibelle.text))
            Dialog.showMessage(contents.head, "Enregistrement effectué avec succès !", title="Message de confirmation")
          }
        }
      }
    }
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }
  centerOnScreen()
  size = new Dimension(350, 180)

}
