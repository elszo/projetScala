import slick.jdbc.H2Profile.api._

import javax.swing.ImageIcon
import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.language.postfixOps
import scala.swing.Font.SansSerif
import scala.swing.Font.Style.BoldItalic
import scala.swing._

class FenAdmin extends Frame{
  title = "Gérer les Administrateurs"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  val lab = new Label("Espace Administrateurs")
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
    def p = new FenAjoutAdmin
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

  var profs = Await.result(acces.tousAdmins, Duration.Inf)
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
    contents += new Label("MODIFIER"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }
    contents += new Label("SUPPRIMER"){
      peer.setOpaque(true)
      peer.setBackground(new Color(100, 149, 237))
    }

    for (p <- profs){
      contents += new Label(p.idAdmin.toString){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(p.prenomAdmin){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Label(p.nomAdmin){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
      contents += new Button("modifier"){
        peer.setBackground(new Color(46, 139, 87))
      }
      contents += new Button("supprimer"){
        peer.setBackground(new Color(139, 0, 0))
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
