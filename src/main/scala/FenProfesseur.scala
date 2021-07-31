import caseClasses.Professeur
import slick.jdbc.H2Profile.api._

import javax.swing.ImageIcon
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.language.postfixOps
import scala.swing.Font.SansSerif
import scala.swing.Font.Style.BoldItalic
import scala.swing._

class FenProfesseur extends Frame{
  title = "Gérer les Professeurs"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  val lab = new Label("Espace Professeurs")
  lab.font = Font(SansSerif, BoldItalic, 30)
  lab.foreground = new Color(105,105,105)
  lab.xLayoutAlignment = 50.0

  var textFieldRecherche = new TextField("Prénom"){
    listenTo(this)
    reactions +={
      case _: event.ValueChanged => {

        
      }
    }
  }

  var textFieldRecherche1 = new TextField("Nom")
  val boutonRecherche = new Button("chercher")
  val labelIconAjouter = new Label()
  labelIconAjouter.icon = new ImageIcon(getClass.getResource("images/user.png"))
  val boutonAjouter = new Button("Nouveau"){
    def p = new FenAjoutProf
    listenTo(mouse.clicks)
    reactions += {
      case _: event.MouseClicked => p.open()
    }
  }
  val panelBouton = new BoxPanel(Orientation.Horizontal){
    contents += labelIconAjouter
    contents += boutonAjouter
    contents += Swing.HStrut(300)
    contents += textFieldRecherche
    contents += textFieldRecherche1
    contents += boutonRecherche
  }
  panelBouton.maximumSize = new Dimension(800, 20)

  var profs = Await.result(acces.tousProfesseurs, Duration.Inf)
  var gridProf = new GridPanel(profs.length+1, 7) {
    contents += new Label("IDENTIFIANT"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    contents += new Label("PRENOM"){
      peer.setOpaque(true)
      background = new Color(100, 149, 237)
    }
    contents += new Label("NOM"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    contents += new Label("GRADE"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    contents += new Label("MATIERE"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    contents += new Label("MODIFIER"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    contents += new Label("SUPPRIMER"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }

    for (p <- profs){
      contents += new Label(p.idProf.toString){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(p.prenomProf){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(p.nomProf){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(p.grade){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(p.matiereProf){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Button("modifier"){
        peer.setBackground(new Color(46, 139, 87))
        listenTo(mouse.clicks)
        reactions += {
          case _: event.MouseClicked => { def fenModif = new Frame{
              title = "Modification de professeurs"
              val textFieldPrenom = new TextField(p.prenomProf)
              val textFieldNom = new TextField(p.nomProf)
              val textFieldGrade = new TextField(p.grade)
              val comboBoxMatiere = new TextField(p.matiereProf)
              val labelPrenom = new Label("Prénom : ")
              val labelNom = new Label("Nom : ")
              val labelGrade = new Label("Grade : ")
              val labelMatiere = new Label("Matière : ")

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
                        textFieldGrade.text.isEmpty | comboBoxMatiere.text.isEmpty){
                        Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title="Message d'erreur")
                      }
                      else {
                        acces.modifierProfesseur(Professeur(p.idProf, textFieldNom.text, textFieldPrenom.text,
                          textFieldGrade.text, comboBoxMatiere.text))
                        Dialog.showMessage(contents.head, "Modification effectué avec succès !", title="Message de confirmation")

                      }
                    }
                  }
                }
                border = Swing.EmptyBorder(10, 10, 10, 10)
              }
              centerOnScreen()
              size = new Dimension(350, 250)
            }
            fenModif.open()
          }
        }
      }
      contents += new Button("supprimer"){

        peer.setBackground(new Color(139, 0, 0))
        listenTo(mouse.clicks)
        reactions += {
          case _: event.MouseClicked => {
            val res = Dialog.showConfirmation(contents.head,"Voulez-vous vraiment supprimer ce Professeur ?",
              optionType=Dialog.Options.YesNo, title="Confirmation de la suppression")
            if (res == Dialog.Result.Ok){
              acces.supprimerProfesseur(p.idProf)
              Dialog.showMessage(contents.head, "le professeur "+p.prenomProf+" "+p.nomProf+" a été supprimer")
            }
          }
        }
      }
    }
  }

  var panelScrol = new ScrollPane(gridProf){peer.setMaximumSize(new Dimension(900, 100))}
  contents = new BoxPanel(Orientation.Vertical){
    contents += lab
    contents += Swing.VStrut(30)
    contents += panelBouton
    contents += Swing.VStrut(5)
    contents += panelScrol
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  size = new Dimension(750, 550)
}
