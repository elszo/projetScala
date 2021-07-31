import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.duration.Duration
import scala.swing._

class FenAffichageEmploiTemps extends  Frame{
  title = "Affiche l'emploi du temps"

  val db = Database.forConfig("h2")
  val acces = new AccesDonnees(db)

  val comboBoxClasse = new ComboBox(List("6ième","5ième","4ième","3ième"))
  val bouton = new Button("afficher"){
    listenTo(mouse.clicks)
    reactions += {
      case _: event.MouseClicked => {
        def fen = new Frame{
          val em = acces.afficherEmploiTempsParClasse(comboBoxClasse.selection.item)
          val lundi = Await.result(em._1, Duration.Inf)
          val mardi = Await.result(em._2, Duration.Inf)
          val mercredi = Await.result(em._3, Duration.Inf)
          val jeudi = Await.result(em._4, Duration.Inf)
          val vendredi = Await.result(em._5, Duration.Inf)
          val samedi = Await.result(em._6, Duration.Inf)
          val gridEmploi = new GridPanel(4, 6){
            contents += new Label("Lundi") {
              peer.setOpaque(true)
              peer.setBackground(new Color(100, 149, 237))
            }
            contents += new Label("Mardi") {
              peer.setOpaque(true)
              background = new Color(100, 149, 237)
            }
            contents += new Label("Mercredi") {
              peer.setOpaque(true)
              peer.setBackground(new Color(100, 149, 237))
            }
            contents += new Label("Jeudi") {
              peer.setOpaque(true)
              peer.setBackground(new Color(100, 149, 237))
            }
            contents += new Label("Vendredi") {
              peer.setOpaque(true)
              peer.setBackground(new Color(100, 149, 237))
            }
            contents += new Label("Samedi") {
              peer.setOpaque(true)
              peer.setBackground(new Color(100, 149, 237))
            }

            for(x <- 1 to 4){
              try{
                contents += new Label(lundi(x).heureDebut+"-"+lundi(x).heureFin+" : "+lundi(x).codeMatiereEmploi){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
              } catch { case e: Exception => contents += new Label(""){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}}

              try{
                contents += new Label(mardi(x).heureDebut+"-"+mardi(x).heureFin+" : "+mardi(x).codeMatiereEmploi){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
              } catch { case e: Exception => contents += new Label(""){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}}

              try{
                contents += new Label(mercredi(x).heureDebut+"-"+mercredi(x).heureFin+" : "+mercredi(x).codeMatiereEmploi){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
              } catch { case e: Exception => contents += new Label(""){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}}

              try{
                contents += new Label(jeudi(x).heureDebut+"-"+jeudi(x).heureFin+" : "+jeudi(x).codeMatiereEmploi){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
              } catch { case e: Exception => contents += new Label(""){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}}

              try{
                contents += new Label(vendredi(x).heureDebut+"-"+vendredi(x).heureFin+" : "+vendredi(x).codeMatiereEmploi){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
              } catch { case e: Exception => contents += new Label(""){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}}

              try{
                contents += new Label(samedi(x).heureDebut+"-"+samedi(x).heureFin+": "+samedi(x).codeMatiereEmploi){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}
              } catch { case e: Exception => contents += new Label(""){peer.setBorder(Swing.MatteBorder(0, 1, 1, 1, new Color(211, 211, 211)))}}
            }

          }
          contents = new BoxPanel(Orientation.Vertical){
            contents += new Label("Emploi du temps de la "+comboBoxClasse.selection.item)
            contents += Swing.VStrut(5)
            contents += gridEmploi
          }
          size = new Dimension(700, 300)
        }
        fen.open()
      }
    }
  }

  contents = new BoxPanel(Orientation.Horizontal){
    contents += comboBoxClasse
    contents += bouton
  }
  centerOnScreen()
  size = new Dimension(200, 70)
}
