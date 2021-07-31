import caseClasses.Matiere
import slick.jdbc.H2Profile.api._

import scala.swing._

class FenAjoutMatiere extends Frame{
  title = "Ajouter une nouvelle Matière"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  var textFieldCode = new TextField()
  var textFieldLibelle = new TextField()
  var comboBoxCoef = new ComboBox(List("", "1","2","3","4","5","6","8"))
  var labelCode = new Label("Code : ")
  var labelLibelle = new Label("Libellé : ")
  val labelCoef = new Label("Coefficient")

  val panelCode = new BoxPanel(Orientation.Horizontal){
    contents += labelCode
    contents += Swing.HStrut(10)
    contents += textFieldCode
  }
  val panelLibelle = new BoxPanel(Orientation.Horizontal){
    contents += labelLibelle
    contents += Swing.HStrut(2)
    contents += textFieldLibelle
  }
  val panelCoef = new BoxPanel(Orientation.Horizontal){
    contents += labelCoef
    contents += comboBoxCoef
  }

  contents = new BoxPanel(Orientation.Vertical){
    contents += panelCode
    contents += Swing.VStrut(5)
    contents += panelLibelle
    contents += Swing.VStrut(5)
    contents += panelCoef
    contents += Swing.VStrut(5)
    contents += new Button("enregistrer"){
      listenTo(mouse.clicks)
      reactions += {
        case _: event.MouseClicked => {
          if(textFieldCode.text.isEmpty | textFieldLibelle.text.isEmpty | comboBoxCoef.selection.item.isEmpty){
            Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title="Message d'erreur")
          }
          else {

            acces.ajouterMatiere(Matiere(textFieldCode.text, textFieldLibelle.text,
              comboBoxCoef.selection.item.toInt))
            Dialog.showMessage(contents.head, "Enregistrement effectué avec succès !", title="Message de confirmation")
          }
        }
      }
    }
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }
  centerOnScreen()
  size = new Dimension(350, 200)

}
