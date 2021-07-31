import caseClasses._
import slick.jdbc.H2Profile.api._

import scala.concurrent.Future

trait InitialData {
  self: SchemaBD =>

  def db: Database

  def insertInitialData(): Future[Unit] = {
    val requetes = DBIO.seq(
      //admins.delete, etudiantMatieres.delete, etudiants.delete, emplois.delete, salles.delete,professeurs.delete, matieres.delete,

      salles += Salle(1, "C0"),
      salles += Salle(2, "C0"),
      salles += Salle(3, "D0"),
      salles += Salle(4, "D0"),

      matieres ++= Seq(
        Matiere("H-G", "Histoire Géographie", 2),
        Matiere("MTH", "Mathématiques", 3),
        Matiere("ANG", "Anglais", 2)
      ),

      professeurs ++= Seq(
        Professeur(1, "Coly", "Omar", "vacataire", "ANG"),
        Professeur(2, "Diop", "Aliou", "professeur", "H-G"),
        Professeur(3, "Diatta", "Romeo", "docteur", "MTH")
      ),

      emplois ++= Seq(
        EmploiTemps(1, 1, 2, "H-G", "Lundi", "8h00", "10h00", "3ième"),
        EmploiTemps(2, 1, 1, "ANG", "Lundi", "10h00", "12h00", "4ième"),
        EmploiTemps(3, 2, 3, "MTH", "Lundi", "8h00", "10h00", "5ième"),
        EmploiTemps(4, 4, 2, "H-G", "Lundi", "10h00", "12h00", "5ième"),
        EmploiTemps(5, 1, 1, "ANG", "Mardi", "10h00", "12h00", "3ième"),
        EmploiTemps(6, 2, 3, "MTH", "Mardi", "8h00", "10h00", "3ième")
      ),

      etudiants ++= Seq(
        Etudiant(1, "Diallo", "Abdou", "3ième"),
        Etudiant(2, "Manga", "Raymond", "3ième"),
        Etudiant(3, "Diatta", "César", "4ième"),
        Etudiant(4, "Diop", "Modou", "4ième"),
        Etudiant(5, "Ndiaye", "Awa", "5ième"),
        Etudiant(6, "Mane", "Sadio", "5ième")
      ),

      etudiantMatieres ++= Seq(
        EtudiantMatiere(1, "H-G", 12, 8.5, 10),
        EtudiantMatiere(2, "H-G", 10, 14.5, 12.5),
        EtudiantMatiere(3, "H-G", 9, 10, 11),
        EtudiantMatiere(4, "H-G", 15, 16, 15.5),
        EtudiantMatiere(5, "H-G", 10, 8, 12.5),
        EtudiantMatiere(6, "H-G", 7.5, 15.5, 11.5),
        EtudiantMatiere(1, "MTH", 12, 8.5, 10),
        EtudiantMatiere(2, "MTH", 10, 14.5, 12.5),
        EtudiantMatiere(3, "MTH", 9, 10, 11),
        EtudiantMatiere(4, "MTH", 15, 16, 15.5),
        EtudiantMatiere(5, "MTH", 10, 8, 12.5),
        EtudiantMatiere(6, "MTH", 7.5, 15.5, 11.5)
      ),

      admins += Admin(1, "admin1", "passe123", "Cisse", "Fatou", "14/07/2021", " ")
    )
    db.run(requetes)
  }
}
