import caseClasses.EmploiTemps
import cats.implicits.catsSyntaxEq
import slick.jdbc.H2Profile.api._

import javax.swing.ImageIcon
import scala.collection.mutable.ListBuffer
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.language.postfixOps
import scala.swing.Font.SansSerif
import scala.swing.Font.Style.BoldItalic
import scala.swing._

class FenSalleMatiere extends Frame{
  title = "Gérer les Salles et les Matières"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  val lab = new Label("Espace Salle et Matière")
  lab.font = Font(SansSerif, BoldItalic, 30)
  lab.foreground = new Color(105,105,105)
  lab.xLayoutAlignment = 50.0

  val labelIconAjouter = new Label()
  labelIconAjouter.icon = new ImageIcon(getClass.getResource("images/user.png"))
  val labelIconAjouterS = new Label()
  labelIconAjouterS.icon = new ImageIcon(getClass.getResource("images/user.png"))
  val boutonAjouterSalle = new Button("Nouveau"){
    def p = new FenAjoutSalle
    listenTo(mouse.clicks)
    reactions += {
      case _: event.MouseClicked => p.open()
    }
  }
  val panelAjouterSalle = new BoxPanel(Orientation.Horizontal){
    contents += labelIconAjouterS
    contents += boutonAjouterSalle
  }

  val comboBoxJour = new ComboBox(List("","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi"))
  val comboBoxDebut = new ComboBox(List("","8h00","10h00","15h00"))
  val comboBoxFin = new ComboBox(List("","10h00","12h00","17h00","18h00"))
  labelIconAjouter.icon = new ImageIcon(getClass.getResource("images/user.png"))
  val boutonSalleLibre = new Button("Salle liblre"){
    listenTo(mouse.clicks)
    reactions += {
      case _: event.MouseClicked =>{
        if(comboBoxJour.selection.item.isEmpty | comboBoxDebut.selection.item.isEmpty |
        comboBoxFin.selection.item.isEmpty){
          Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title="Message d'erreur")
        }
        else {

          def fenSalleLibre = new Frame{
            title = "Liste des salles libres"
            val salleLibre = Await.result(acces.rechercheSalleLibre(comboBoxJour.selection.item,
              comboBoxDebut.selection.item, comboBoxFin.selection.item), Duration.Inf)
            val gridSalleLibre = new GridPanel(salleLibre.length+1, 2){
              contents += new Label("Numéro"){
                peer.setOpaque(true)
                peer.setBackground(new Color(100, 149, 237))
              }
              contents += new Label("Libellé"){
                peer.setOpaque(true)
                peer.setBackground(new Color(100, 149, 237))
              }
              for(sal <- salleLibre) {
                contents += new Label(sal._1.toString){peer.setBorder(Swing.MatteBorder(0,1,1,1, new Color(211,211,211)))}
                contents += new Label(sal._2){peer.setBorder(Swing.MatteBorder(0,1,1,1, new Color(211,211,211)))}
              }
            }
            contents = new ScrollPane(gridSalleLibre){peer.setMaximumSize(new Dimension(500, 200))}
            size = new Dimension(500, 200)
            centerOnScreen()
          }

          fenSalleLibre.open()
        }
      }
    }
  }
  val panelJour = new BoxPanel(Orientation.Horizontal){
    contents += new Label("Jour : ")
    contents += comboBoxJour
  }
  val panelDebut = new BoxPanel(Orientation.Horizontal){
    contents += new Label("Heure début : ")
    contents += comboBoxDebut
  }
  val panelFin = new BoxPanel(Orientation.Horizontal){
    contents += new Label("Heure fin : ")
    contents += comboBoxFin
  }
  val panelSalleLibre = new BoxPanel(Orientation.Vertical){
    contents += panelJour
    contents += Swing.VStrut(5)
    contents += panelDebut
    contents += Swing.VStrut(5)
    contents += panelFin
    contents += Swing.VStrut(5)
    contents += boutonSalleLibre
  }

  val boutonSalleOccuper = new Button("Salle occupée"){
    listenTo(mouse.clicks)
    reactions += {
      case _: event.MouseClicked =>{
        def fenSalleOccupe = new Frame{
          title = "Liste des salles occupées"
          val salleOcuppe = Await.result(acces.salleOccuper, Duration.Inf)
          val gridSalleLibre = new GridPanel(salleOcuppe.length+1, 4){
            contents += new Label("Numéro Salle"){
              peer.setOpaque(true)
              peer.setBackground(new Color(100, 149, 237))
            }
            contents += new Label("Libellé Salle"){
              peer.setOpaque(true)
              peer.setBackground(new Color(100, 149, 237))
            }
            contents += new Label("Professeur"){
              peer.setOpaque(true)
              peer.setBackground(new Color(100, 149, 237))
            }
            contents += new Label("Matiere"){
              peer.setOpaque(true)
              peer.setBackground(new Color(100, 149, 237))
            }
            for(sal <- salleOcuppe) {
              contents += new Label(sal._1.toString){peer.setBorder(Swing.MatteBorder(0,1,1,1, new Color(211,211,211)))}
              contents += new Label(sal._2){peer.setBorder(Swing.MatteBorder(0,1,1,1, new Color(211,211,211)))}
              contents += new Label(sal._3){peer.setBorder(Swing.MatteBorder(0,1,1,1, new Color(211,211,211)))}
              contents += new Label(sal._4){peer.setBorder(Swing.MatteBorder(0,1,1,1, new Color(211,211,211)))}
            }
          }
          contents = new ScrollPane(gridSalleLibre){peer.setMaximumSize(new Dimension(500, 200))}
          size = new Dimension(500, 200)
          centerOnScreen()
        }

        fenSalleOccupe.open()
      }
    }
  }
  val BoxpanelSalle = new BoxPanel(Orientation.Vertical){
    contents += new Label("Salles"){
      peer.setFont(Font(SansSerif, BoldItalic, 20))
      peer.setForeground(new Color(105,105,105))
      peer.setAlignmentX(-30)
    }
    contents += Swing.VStrut(15)
    contents += panelAjouterSalle
    contents += Swing.VStrut(15)
    contents += panelSalleLibre
    contents += Swing.VStrut(15)
    contents += boutonSalleOccuper
    contents += Swing.VStrut(5)
    border = Swing.MatteBorder(4, 4, 4, 4, new Color(40, 40, 40))
    peer.setSize(new Dimension(200, 300))
  }

  val boutonAjouterMatiere= new Button("Nouveau") {
    def p = new FenAjoutMatiere

    listenTo(mouse.clicks)
    reactions += {
      case _: event.MouseClicked => p.open()
    }
  }
  val panelAjouterMatiere = new BoxPanel(Orientation.Horizontal){
    contents += labelIconAjouter
    contents += boutonAjouterMatiere
  }
  val mat = Await.result(acces.toutesMatieres, Duration.Inf)
  val gridPanleMatiere = new GridPanel(mat.length+1, 2){
    contents += new Label("Code Matière"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    contents += new Label("Libellé"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    contents += new Label("Coefficient"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    for (m <- mat) {
      contents += new Label(m.codeMatiere){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(m.libelleMatiere){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(m.coefficient.toString){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
    }
  }
  val BoxpanelMatiere = new BoxPanel(Orientation.Vertical){
    contents += new Label("Matières"){
      peer.setFont(Font(SansSerif, BoldItalic, 20))
      peer.setForeground(new Color(105,105,105))
      peer.setAlignmentX(-30)
    }
    contents += Swing.VStrut(15)
    contents += panelAjouterMatiere
    contents += Swing.VStrut(10)
    contents += new ScrollPane(gridPanleMatiere){peer.setMaximumSize(new Dimension(500, 200))}
    contents += Swing.VStrut(5)
    border = Swing.MatteBorder(4, 4, 4, 4, new Color(40, 40, 40))
    peer.setSize(new Dimension(300, 300))
  }

  val comboBoxJourAl = new ComboBox(List("","Lundi","Mardi","Mercredi","Jeudi","Vendredi","Samedi"))
  val comboBoxDebutAl = new ComboBox(List("","8h00","10h00","15h00"))
  val comboBoxFinAl = new ComboBox(List("","10h00","12h00","17h00","18h00"))
  val panelHeure = new BoxPanel(Orientation.Horizontal){
    contents += new Label("Jour et Heure : ")
    contents += comboBoxJourAl
    contents += Swing.HStrut(2)
    contents += comboBoxDebutAl
    contents += Swing.HStrut(2)
    contents += comboBoxFinAl
  }
  var  listeSalle = ListBuffer[String]()
  listeSalle += ""
  val sals = Await.result(acces.toutesSalles, Duration.Inf)
  for(m <- sals){
    listeSalle += m.libelleSalle+" "+m.numeroSalle.toString
  }
  val comboBoxSalle = new ComboBox(listeSalle)
  var  listeMatiere = ListBuffer[String]()
  listeMatiere += ""
  val matiers = Await.result(acces.toutesMatieres, Duration.Inf)
  for(m <- matiers){
    listeMatiere += m.codeMatiere
  }
  val comboBoxMatiere = new ComboBox(listeMatiere)
  val panelSalleMatiere = new BoxPanel(Orientation.Horizontal){
    contents += new Label("Salle : ")
    contents += comboBoxSalle
    contents += Swing.HStrut(10)
    contents += new Label("Matière : ")
    contents += comboBoxMatiere
  }
  var  listeProf = ListBuffer[String]()
  listeProf += ""
  val profs = Await.result(acces.tousProfesseurs, Duration.Inf)
  for(m <- profs){
    listeProf += m.prenomProf+" "+m.nomProf
  }
  val comboBoxProf = new ComboBox(listeProf)
  val comboBoxClasse = new ComboBox(List("6ième","5ième","4ième","3ième"))
  val panelProfClasse = new BoxPanel(Orientation.Horizontal){
    contents += new Label("Prof : ")
    contents += comboBoxProf
    contents += Swing.HStrut(10)
    contents += new Label("Classe : ")
    contents += comboBoxClasse
  }
  val boutonAllouer= new Button("Valider") {
    listenTo(mouse.clicks)
    reactions += {
      case _: event.MouseClicked => {
        if (comboBoxJourAl.selection.item.isEmpty | comboBoxDebutAl.selection.item.isEmpty | comboBoxFinAl.selection.item.isEmpty |
          comboBoxSalle.selection.item.isEmpty | comboBoxMatiere.selection.item.isEmpty |
          comboBoxProf.selection.item.isEmpty | comboBoxClasse.selection.item.isEmpty){
          Dialog.showMessage(contents.head, "Vous devez remplir tous les champs", title="message d'erreur")
        }
        else {
          val emp = Await.result(acces.afficherEmploiTemps, Duration.Inf)
          var verif: Int = 0
          val splitSal = comboBoxSalle.selection.item.split(" ")
          val numSal = splitSal(1)
          val splitProf = comboBoxProf.selection.item.split(" ")
          val prenomP = splitProf(0)
          val nomP = splitProf(1)
          val numProf = profs.filter(_.nomProf===nomP).filter(_.prenomProf===prenomP).map(_.idProf)
          for(m <- emp){
            val sal = sals.filter(_.numeroSalle===numSal.toInt)
            val sa = sal(0)
            val pro = profs.filter(_.idProf===numProf(0))
            val p = pro(0)
            if(m.numeroSalleEmploi===sa.numeroSalle && m.dateEmploi===comboBoxJourAl.selection.item
              && m.heureDebut===comboBoxDebutAl.selection.item && m.heureFin===comboBoxFinAl.selection.item){
              verif = 1
            }
            else if (m.idProfEmploi===p.idProf && m.dateEmploi===comboBoxJourAl.selection.item
              && m.heureDebut===comboBoxDebutAl.selection.item && m.heureFin===comboBoxFinAl.selection.item){
              verif = 2
            }
            else if (m.niveau===comboBoxClasse.selection.item && m.dateEmploi===comboBoxJourAl.selection.item
              && m.heureDebut===comboBoxDebutAl.selection.item && m.heureFin===comboBoxFinAl.selection.item){
              verif = 3
            }
          }
          if (verif === 1){
            Dialog.showMessage(contents.head,"Cette salle est déjà prise", title = "message d'erreur")
          }
          else if (verif === 2){
            Dialog.showMessage(contents.head,"Ce prof n'est pas libre à cette heure", title = "message d'erreur")
          }
          else if (verif === 3){
            Dialog.showMessage(contents.head,"Cette classe a cours à cette heure", title = "message d'erreur")
          }
          else {
            acces.ajouterEmploiTemps(EmploiTemps(1, numSal.toInt, numProf(0), comboBoxMatiere.selection.item,
              comboBoxJourAl.selection.item, comboBoxDebutAl.selection.item, comboBoxFinAl.selection.item,
              comboBoxClasse.selection.item))
            Dialog.showMessage(contents.head, "Enregistrement effectué avec succès", title="Confirmation")
          }
        }
      }
    }
  }
  val panelAllocationSalle = new BoxPanel(Orientation.Vertical){
    contents += new Label("Allouer Une Salle"){
      peer.setFont(Font(SansSerif, BoldItalic, 20))
      peer.setForeground(new Color(105,105,105))
      peer.setAlignmentX(-30)
    }
    contents += Swing.VStrut(15)
    contents += panelHeure
    contents += Swing.VStrut(5)
    contents += panelSalleMatiere
    contents += Swing.VStrut(5)
    contents += panelProfClasse
    contents += Swing.VStrut(5)
    contents += boutonAllouer
    contents += Swing.VStrut(5)
    border = Swing.MatteBorder(4, 4, 4, 4, new Color(40, 40, 40))
    peer.setSize(new Dimension(500, 300))
  }

  contents = new BoxPanel(Orientation.Vertical){
    contents += lab
    contents += Swing.VStrut(30)
    contents += new BoxPanel(Orientation.Horizontal){
      contents += BoxpanelSalle
      contents += Swing.HStrut(10)
      contents += BoxpanelMatiere
    }
    contents += Swing.VStrut(15)
    contents += panelAllocationSalle
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }

  size = new Dimension(800, 600)
}
