import slick.jdbc.H2Profile.api._

import java.awt.Color
import java.awt.geom.Arc2D
import scala.swing.Font.SansSerif
import scala.swing.Font.Style.BoldItalic
import scala.swing.{Color, _}
 
class FenAffichageStatistiques extends Frame {
  title = "Afficher les statistiques"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  val lab = new Label("Les Statistiques de l'Ecole")
  lab.font = Font(SansSerif, BoldItalic, 35)
  lab.foreground = new Color(105,105,105)

  val labE = new Label("Les trois meilleurs élèves de l'école :")
  labE.font = Font(SansSerif, BoldItalic, 20)
  labE.foreground = new Color(105,105,105)

  val lesMeilleurs = acces.meilleursEtudiants
  val eleve1 = new Label(lesMeilleurs(0)._2+" : "+lesMeilleurs(0)._3.toString+"  Classe de "+lesMeilleurs(0)._4)
  val eleve2 = new Label(lesMeilleurs(1)._2+" : "+lesMeilleurs(1)._3.toString+"  Classe de "+lesMeilleurs(1)._4)
  val eleve3 = new Label(lesMeilleurs(2)._2+" : "+lesMeilleurs(2)._3.toString+"  Classe de "+lesMeilleurs(2)._4)
  eleve1.font = Font(SansSerif, BoldItalic, 20)
  eleve1.foreground = new Color(100,149,255)

  eleve2.font = Font(SansSerif, BoldItalic, 20)
  eleve2.foreground = new Color(100,149,255)

  eleve3.font = Font(SansSerif, BoldItalic, 20)
  eleve3.foreground = new Color(100,149,255)

  val taux = new Label("Taux d'Echecs et de Réussites")
  taux.font = Font(SansSerif, BoldItalic, 25)
  taux.foreground = new Color(0,0,255)

  val labelReussite = new Label("Taux de réussites")
  val legendReussite = new Label("couleur"){
    peer.setOpaque(true)
    peer.setBackground(new Color(0, 255, 0))
    peer.setForeground(new Color(0, 255, 0))
  }
  val panelReussite = new BoxPanel(Orientation.Horizontal){
    contents += legendReussite
    contents += Swing.HStrut(1)
    contents += labelReussite
    border = Swing.EmptyBorder(5,5,5,5)
  }

  val legendEchec = new Label("couleur"){
    peer.setOpaque(true)
    peer.setBackground(new Color(255, 0, 0))
    peer.setForeground(new Color(255, 0, 0))
  }
  val labelEchec = new Label("Taux d'échecs")
  val panelEchec = new BoxPanel(Orientation.Horizontal){
    contents += legendEchec
    contents += Swing.HStrut(1)
    contents += labelEchec
    border = Swing.EmptyBorder(0, 5, 5, 5)
  }

  val panelLegende = new BoxPanel(Orientation.Vertical){
    contents += panelReussite
    contents += panelEchec
    border = Swing.MatteBorder(2, 2, 2, 2, new Color(211, 211, 211))
    peer.setMaximumSize(new Dimension(170, 100))
  }

  val t = acces.tauxReussiteEchec
  val reussite = t._1
  val echec = t._2
  val angleReussite = ((360*reussite)/100).round
  val angleEchec = ((360*echec)/100).round
  val panelTaux = new BoxPanel(Orientation.Horizontal){

    contents += new Panel {
      peer.setMaximumSize(new Dimension(250,250))
      opaque=true

      override def paint(g: Graphics2D) = {
        //val maxrad=120
        //val myRand=new Random()
        //g.setColor(new Color(0, 255, 0))
        // g.fillOval(150, 50,150,150)

        val arc_pi: Arc2D = new Arc2D.Float(20, 30, 120, 120, 0, angleReussite, Arc2D.PIE)
        val arc: Arc2D = new Arc2D.Float(20, 30, 120, 120, angleReussite, 360-angleReussite, Arc2D.PIE)
        g.setPaint(Color.green)
        g.fill(arc_pi)
        g.setPaint(Color.red)
        g.fill(arc)
        g.setPaint(Color.black)
        g.drawString(reussite.toString+"%", arc_pi.getAngleStart.toInt+90, arc_pi.getCenterY.toInt-5)
        g.setPaint(Color.black)
        g.drawString(echec.toString+"%", arc_pi.getAngleStart.toInt+90, arc_pi.getCenterY.toInt+15)

        //for (x<-1 to maxrad) {
        //g.setColor(new Color(myRand.nextInt(255),myRand.nextInt(255),myRand.nextInt(255)))
        //g.setColor(new Color(0, 255, 0))
        //g.drawOval((size.width-x)/2,(size.height-x)/2,x,x)
        //}
      }
    }
    contents += Swing.HStrut(5)
    contents += panelLegende
    peer.setSize(new Dimension(400, 300))
  }

  val labelMoyenneClasse = new Label("Moyenne de chaque classe : ")
  labelMoyenneClasse.font = Font(SansSerif, BoldItalic, 25)
  labelMoyenneClasse.foreground = new Color(0,0,255)

  val labelC6 = new Label("6ième")
  labelC6.font = Font(SansSerif, BoldItalic, 20)
  labelC6.foreground = new Color(100,149,255)

  val labelC5 = new Label("5ième")
  labelC5.font = Font(SansSerif, BoldItalic, 20)
  labelC5.foreground = new Color(100,149,255)

  val labelC4 = new Label("4ième")
  labelC4.font = Font(SansSerif, BoldItalic, 20)
  labelC4.foreground = new Color(100,149,255)

  val labelC3 = new Label("3ième")
  labelC3.font = Font(SansSerif, BoldItalic, 20)
  labelC3.foreground = new Color(100,149,255)

  val panelMoyenneClasse = new BoxPanel(Orientation.Vertical){
    contents += labelMoyenneClasse
    contents += Swing.VStrut(5)
    contents += new GridPanel(5, 2){
      contents += new Label("Classe") {
        peer.setOpaque(true)
        peer.setBackground(new Color(100, 149, 237))
      }
      contents += new Label("Moyenne") {
        peer.setOpaque(true)
        peer.setBackground(new Color(100, 149, 237))
      }
      contents += labelC3
      contents += new Label(acces.moyenneClasse("3ième").toString)
      contents += labelC4
      contents += new Label(acces.moyenneClasse("4ième").toString)
      contents += labelC5
      contents += new Label(acces.moyenneClasse("5ième").toString)
      contents += labelC6
      contents += new Label(acces.moyenneClasse("6ième").toString)
    }
  }

  contents = new BoxPanel(Orientation.Vertical){
    contents += lab
    contents += Swing.VStrut(30)
    contents += new BoxPanel(Orientation.Vertical){
      contents += labE
    }
    contents += new BoxPanel(Orientation.Vertical){
      contents += Swing.VStrut(15)
      contents += eleve1
      contents += Swing.VStrut(8)
      contents += eleve2
      contents += Swing.VStrut(8)
      contents += eleve3
    }
    contents += Swing.VStrut(25)
    contents += new ScrollPane(panelMoyenneClasse){peer.setMaximumSize(new Dimension(500, 50))}
    contents += Swing.VStrut(25)
    contents += taux
    contents += Swing.VStrut(5)
    contents += panelTaux
    border = Swing.EmptyBorder(10, 10, 10, 10)
  }
  minimumSize = new Dimension(800, 700)

}
