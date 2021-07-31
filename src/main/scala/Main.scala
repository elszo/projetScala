import slick.jdbc.H2Profile.api._

import scala.concurrent.Await
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.language.postfixOps


object Main extends App with SchemaBD with InitialData with Magie {
  val db = Database.forConfig("h2")
  private val future = createSchemaIfNotExists.flatMap(_ => insertInitialData())
  Await.ready(future, Duration.Inf)

  val acces1 = new AccesDonnees(db)
  afficherResultats(acces1.etudiantClasse3)
  afficherResultats(acces1.rechercheSalleLibre("Lundi", "10h00", "12h00"))
  afficherResultats(acces1.salleOccuper)
  afficherResultats(acces1.tousProfesseurs)
  afficherResultats(acces1.listerNotesEtudiant(1))
  println("Moyenne classe : "+acces1.moyenneClasse("3ième"))
  println(acces1.tauxReussiteEchec)
  acces1.meilleursEtudiants
  afficherResultats(acces1.afficherEmploiTemps)
  println("Emploi par classe")
  println(acces1.afficherEmploiTempsParClasse("5ième"))
}
