import caseClasses.{Etudiant, EtudiantMatiere}
import cats.implicits.catsSyntaxEq
import slick.jdbc.H2Profile.api._

import javax.swing.ImageIcon
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.language.postfixOps
import scala.swing.Font.SansSerif
import scala.swing.Font.Style.BoldItalic
import scala.swing._

class FenEtudiant extends Frame{
  title = "Gérer les Etudiants"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  val lab = new Label("Espace Etudiant")
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

  val boutonRecherche = new Button("chercher")
  val labelIconAjouter = new Label()
  labelIconAjouter.icon = new ImageIcon(getClass.getResource("images/user.png"))
  val boutonAjouter = new Button("Nouveau"){
    def p = new FenAjoutEtudiant
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
    contents += boutonRecherche
  }
  panelBouton.maximumSize = new Dimension(800, 30)

  var profs = Await.result(acces.tousEtudiants, Duration.Inf)
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
    contents += new Label("Niveau"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    contents += new Label("Détails"){
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
      contents += new Label(p.idEtud.toString){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(p.prenomEtud){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(p.nomEtud){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(p.niveauEtude){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Button("détails"){
        peer.setBackground(new Color(20, 20, 75))
        peer.setForeground(new Color(255, 255, 255))
        listenTo(mouse.clicks)
        reactions +={
          case _: event.MouseClicked => {
            def fenDetail = new Frame{
              title = "Détails"
              var etudMat = Await.result(acces.tousEtudiantMatiere, Duration.Inf)
              etudMat = etudMat.filter(_.idEtuMat===p.idEtud)
              val gridEtudMat = new GridPanel(etudMat.length+1, 7) {
                contents += new Label("Matières") {
                  peer.setOpaque(true)
                  peer.setBackground(new Color(100, 149, 237))
                }
                contents += new Label("Devoir 1") {
                  peer.setOpaque(true)
                  background = new Color(100, 149, 237)
                }
                contents += new Label("Devoir 2") {
                  peer.setOpaque(true)
                  peer.setBackground(new Color(100, 149, 237))
                }
                contents += new Label("Examen") {
                  peer.setOpaque(true)
                  peer.setBackground(new Color(100, 149, 237))
                }
                contents += new Label("Coefficient") {
                  peer.setOpaque(true)
                  peer.setBackground(new Color(100, 149, 237))
                }
                contents += new Label("Moyenne") {
                  peer.setOpaque(true)
                  peer.setBackground(new Color(100, 149, 237))
                }
                contents += new Label("MODIFIER Notes") {
                  peer.setOpaque(true)
                  peer.setBackground(new Color(100, 149, 237))
                }
                for (etMat <- etudMat) {
                  val coef = Await.result(acces.toutesMatieres, Duration.Inf)
                  val coeficient = coef.filter(_.codeMatiere === etMat.codeMatiereEtud).map(_.coefficient)
                  val moyenne = (((etMat.devoir1 + etMat.devoir2) / 2) + etMat.examen) / 2
                  contents += new Label(etMat.codeMatiereEtud) {
                    peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))
                  }
                  contents += new Label(etMat.devoir1.toString) {
                    peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))
                  }
                  contents += new Label(etMat.devoir2.toString) {
                    peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))
                  }
                  contents += new Label(etMat.examen.toString) {
                    peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))
                  }
                  contents += new Label(coeficient(0).toString) {
                    peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))
                  }
                  contents += new Label(moyenne.toString) {
                    peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))
                  }
                  contents += new Button("modifier notes") {
                    listenTo(mouse.clicks)
                    reactions += {
                      case _: event.MouseClicked => {
                        def fenModifNote = new Frame {
                          title = "Modification des notes"
                          val textFieldDevoir1 = new TextField(etMat.devoir1.toString)
                          val textFieldDevoir2 = new TextField(etMat.devoir2.toString)
                          val textFieldExamen = new TextField(etMat.examen.toString)
                          val labelPrenom = new Label("Devoir_1 : ")
                          val labelNom = new Label("Devoir_2 : ")
                          val labelMatiere = new Label("Examen : ")

                          val panelNom = new BoxPanel(Orientation.Horizontal) {
                            contents += labelNom
                            contents += textFieldDevoir2
                          }
                          val panelPrenom = new BoxPanel(Orientation.Horizontal) {
                            contents += labelPrenom
                            contents += textFieldDevoir1
                          }
                          val panelMatiere = new BoxPanel(Orientation.Horizontal) {
                            contents += labelMatiere
                            contents += Swing.HStrut(3)
                            contents += textFieldExamen
                          }

                          contents = new BoxPanel(Orientation.Vertical) {
                            contents += panelPrenom
                            contents += Swing.VStrut(5)
                            contents += panelNom
                            contents += Swing.VStrut(5)
                            contents += panelMatiere
                            contents += Swing.VStrut(5)
                            contents += new Button("enregistrer") {
                              listenTo(mouse.clicks)
                              reactions += {
                                case _: event.MouseClicked => {
                                  if (textFieldDevoir1.text.isEmpty | textFieldDevoir2.text.isEmpty |
                                    textFieldExamen.text.isEmpty) {
                                    Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title = "Message d'erreur")
                                  }
                                  else {
                                    acces.modifierEtudiantMatiere(EtudiantMatiere(etMat.idEtuMat, etMat.codeMatiereEtud,
                                      textFieldDevoir1.text.toDouble, textFieldDevoir2.text.toDouble,
                                      textFieldExamen.text.toDouble))
                                    Dialog.showMessage(contents.head, "Modification effectué avec succès !", title = "Message de confirmation")

                                  }
                                }
                              }
                            }
                            border = Swing.EmptyBorder(10, 10, 10, 10)
                          }
                          centerOnScreen()
                          size = new Dimension(350, 250)
                        }

                        fenModifNote.open()
                      }
                    }
                  }
                }
              }
              val labEtud = new Label(p.prenomEtud+" "+p.nomEtud+" : "+p.niveauEtude)
              labEtud.font = Font(SansSerif, BoldItalic, 30)
              labEtud.foreground = new Color(105,105,105)
              labEtud.xLayoutAlignment = 1
              val moy = acces.moyenneGlobaleEtudiant(p.idEtud)

              val labMoy = new Label("Moyenne de l'élève : "+moy.toString)
              labMoy.font = Font(SansSerif, BoldItalic, 20)
              labMoy.foreground = new Color(105,105,105)

              val labelMoyenneClasse = new Label("Moyenne de la classe : "+acces.moyenneClasse(p.niveauEtude))
              labelMoyenneClasse.font = Font(SansSerif, BoldItalic, 20)
              labelMoyenneClasse.foreground = new Color(105,105,105)

              val panelMoyenne = new BoxPanel(Orientation.Horizontal){
                contents += labMoy
                contents += Swing.HStrut(300)
                contents += labelMoyenneClasse
                peer.setMaximumSize(new Dimension(850, 20))
              }

              contents = new BoxPanel(Orientation.Vertical){
                contents += labEtud
                contents += Swing.VStrut(20)
                contents += panelMoyenne
                contents += Swing.VStrut(30)
                contents += new ScrollPane(gridEtudMat){peer.setMaximumSize(new Dimension(2500, 500))}
                border = Swing.EmptyBorder(10, 10, 10, 10)
              }
              size = new Dimension(900, 500)
              centerOnScreen()
            }
            fenDetail.open()
          }
        }
      }
      contents += new Button("modifier"){
        peer.setBackground(new Color(46, 139, 87))
        peer.setForeground(new Color(255, 255, 255))
        listenTo(mouse.clicks)
        reactions += {
          case _: event.MouseClicked => { def fenModif = new Frame{
            title = "Modification de professeurs"
            val textFieldPrenom = new TextField(p.prenomEtud)
            val textFieldNom = new TextField(p.nomEtud)
            val comboBoxMatiere = new TextField(p.niveauEtude)
            val labelPrenom = new Label("Prénom : ")
            val labelNom = new Label("Nom : ")
            val labelMatiere = new Label("Classe : ")

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
                      comboBoxMatiere.text.isEmpty){
                      Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title="Message d'erreur")
                    }
                    else {
                      acces.modifierEtudiant(Etudiant(p.idEtud, textFieldNom.text, textFieldPrenom.text,
                        comboBoxMatiere.text))
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
        peer.setForeground(new Color(255, 255, 255))
        peer.setBackground(new Color(139, 0, 0))
        listenTo(mouse.clicks)
        reactions += {
          case _: event.MouseClicked => {
            val res = Dialog.showConfirmation(contents.head,"Voulez-vous vraiment supprimer ce Professeur ?",
              optionType=Dialog.Options.YesNo, title="Confirmation de la suppression")
            if (res == Dialog.Result.Ok){
              acces.supprimerEtudiant(p.idEtud)
              Dialog.showMessage(contents.head, "l'étudiant "+p.prenomEtud+" "+p.nomEtud+" a été supprimer")
            }
          }
        }
      }
    }
  }

  var panelScrol = new ScrollPane(gridProf){peer.setMaximumSize(new Dimension(900, 500))}
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
