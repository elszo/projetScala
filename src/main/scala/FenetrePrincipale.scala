import javax.swing.ImageIcon
import scala.swing.Font.SansSerif
import scala.swing.Font.Style.BoldItalic
import scala.swing._

class FenetrePrincipale extends MainFrame{
  title = "Gestion de l'établissement"

  menuBar = new MenuBar {
    contents += new Menu("Gestion des composants") {
      contents += new MenuItem(Action("Professeurs") {
        def fenProf = new FenProfesseur
        fenProf.open()
      })
      contents += new MenuItem(Action("Etudiants") {
        def fen = new FenEtudiant
        fen.open()
      })
      contents += new MenuItem(Action("Salles et Matières") {
        def fen = new FenSalleMatiere
        fen.open()
      })
      contents += new MenuItem(Action("Administrateurs") {
        def fen = new FenAdmin
        fen.open()
      })
      contents += new Separator
      contents += new MenuItem(Action("QUITTER") {
        sys.exit(0)
      })
    }
    contents += new Menu("Emploi du Temps"){
      contents += new MenuItem(Action("Allouer une salle") {
        def fen = new FenSalleMatiere
        fen.open()
      })
      contents += new MenuItem(Action("Afficher l'emploi du temps") {
        def fen = new FenAffichageEmploiTemps
        fen.open()
      })
      contents += new Separator
      contents += new MenuItem(Action("Afficher les statistiques") {
        def fen = new FenAffichageStatistiques
        fen.open()
      })
    }

  }
  val labelNomEcole = new Label("Collège Saint Charles LWANGA")
  labelNomEcole.font = Font(SansSerif, BoldItalic, 40)
  labelNomEcole.foreground = new Color(30,144,255)
  val labelIconLogo = new Label()
  labelIconLogo.icon = new ImageIcon(getClass.getResource("images/logolwanga.png"))

  val labelDescription = new Label("Historique de l'établissement")
  labelDescription.font = Font(SansSerif, BoldItalic, 30)
  labelDescription.foreground = new Color(105,105,105)
  val labelIconDesc  = new Label()
  labelIconDesc.icon = new ImageIcon(getClass.getResource("images/college.png"))

  def femploi = new FenAffichageEmploiTemps
  val labelEmploi = new Label("Afficher l'emploi du temps"){
    listenTo(mouse.clicks)
    reactions += {
      case _: event.MouseClicked => femploi.open()
    }
    listenTo(mouse.moves)
    reactions +={
      case _: event.MouseEntered => peer.setForeground(new Color(0, 0, 255))
      case _: event.MouseExited => peer.setForeground(new Color(105,105,105))
    }
  }

  labelEmploi.font = Font(SansSerif, BoldItalic, 30)
  labelEmploi.foreground = new Color(105,105,105)
  val labelIconEmploi = new Label()
  labelIconEmploi.icon = new ImageIcon(getClass.getResource("images/emploi.png"))

  def fstat = new FenAffichageStatistiques
  val labelStat = new Label("Afficher les satistiques de l'école"){
    listenTo(mouse.clicks)
    reactions += {
      case _: event.MouseClicked => fstat.open()
    }
    listenTo(mouse.moves)
    reactions +={
      case _: event.MouseEntered => peer.setForeground(new Color(0, 0, 255))
      case _: event.MouseExited => peer.setForeground(new Color(105,105,105))
    }
  }
  labelStat.font = Font(SansSerif, BoldItalic, 30)
  labelStat.foreground = new Color(105,105,105)
  val labelIconStat = new Label()
  labelIconStat.icon = new ImageIcon(getClass.getResource("images/stat.png"))

  contents = new BoxPanel(Orientation.Vertical){
    contents += new BoxPanel(Orientation.Horizontal){
      contents += labelIconLogo
      contents += Swing.HStrut(5)
      contents += labelNomEcole
    }
    contents += Swing.VStrut(25)
    contents += new BoxPanel(Orientation.Horizontal){
      contents += labelIconDesc
      contents += Swing.HStrut(10)
      contents += labelDescription
    }
    contents += Swing.VStrut(5)
    contents += new BoxPanel(Orientation.Horizontal){
      contents += labelEmploi
      contents += Swing.HStrut(10)
      contents += labelIconEmploi
    }
    contents += Swing.VStrut(5)
    contents += new BoxPanel(Orientation.Horizontal){
      contents += labelIconStat
      contents += Swing.HStrut(10)
      contents += labelStat
    }
  }
  size = new Dimension(850, 650)
  centerOnScreen()
}

