//import java.sql.Date
//import java.time.{LocalDate, LocalDateTime}

object caseClasses{
  case class Professeur(idProf: Int, nomProf: String, prenomProf: String, grade: String, matiereProf: String)
  case class Matiere(codeMatiere: String, libelleMatiere: String, coefficient: Int)
  case class Salle(numeroSalle: Int, libelleSalle: String)
  case class EmploiTemps(idEmploi: Int, numeroSalleEmploi: Int, idProfEmploi: Int, codeMatiereEmploi: String,
                         dateEmploi: String, heureDebut: String, heureFin: String, niveau: String)
  case class Etudiant(idEtud: Int, nomEtud: String, prenomEtud: String, niveauEtude: String)
  case class EtudiantMatiere(idEtuMat: Int, codeMatiereEtud: String, devoir1: Double, devoir2: Double, examen: Double)
  case class Admin(idAdmin: Int, login: String, motDePasse: String, nomAdmin: String, prenomAdmin: String,
                   dateAjout: String, dateSuppression: String)

}