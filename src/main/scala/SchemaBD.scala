import caseClasses._
import slick.jdbc.H2Profile.api._

trait SchemaBD {

  class Professeurs(tag: Tag) extends Table[Professeur](tag, "PROFESSEUR"){
    def idProf = column[Int]("ID_PROF", O.PrimaryKey, O.AutoInc)
    def nomProf = column[String]("NOME_PROF")
    def prenomProf = column[String]("PRENOM_PROF")
    def grade = column[String]("GRADE")
    def matiereProf = column[String]("MATIERE_PROF")
    def matiereprof = foreignKey("FK_MATIERE_PROF", matiereProf, matieres)(_.codeMatiere)
    def * = (idProf, nomProf, prenomProf, grade, matiereProf) <> (Professeur.tupled, Professeur.unapply)
  }
  val professeurs = TableQuery[Professeurs]

  class Matieres(tag: Tag) extends Table[Matiere](tag, "MATIERE"){
    def codeMatiere = column[String]("CODE_MATIERE", O.PrimaryKey)
    def libelleMatiere = column[String]("LIBELLE_MATIERE")
    def coefficient = column[Int]("COEFFICIENT")
    def * = (codeMatiere, libelleMatiere, coefficient) <> (Matiere.tupled, Matiere.unapply)
  }
  val matieres = TableQuery[Matieres]

  class Salles(tag: Tag) extends Table[Salle](tag, "SALLE"){
    def numeroSalle = column[Int]("NUMERO_SALLE")
    def libelleSalle = column[String]("LIBELLE_SALLE")
    def * = (numeroSalle, libelleSalle) <> (Salle.tupled, Salle.unapply)
  }
  val salles = TableQuery[Salles]


  class Emplois(tag: Tag) extends Table[EmploiTemps](tag, "EMPLOI_TEMPS"){
    def idEmploi = column[Int]("ID_Emploi", O.PrimaryKey, O.AutoInc)
    def numeroSalleEmploi = column[Int]("NUMERO_SALLE_EMPLOI")
    def idProfEmploi = column[Int]("ID_PROF_EMPLOI")
    def codeMatiereEmploi = column[String]("CODE_MATIERE_EMPLOI")
    def dateEmploi = column[String]("DATE_EMPLOI")
    def heureDebut = column[String]("HEURE_DEBUT")
    def heureFin = column[String]("HEURE_FIN")
    def niveau = column[String]("NIVEAU")
    def salle = foreignKey("FK_SALLE", numeroSalleEmploi, salles)(_.numeroSalle)
    def professeur = foreignKey("FK_PROFFESSEUR", idProfEmploi, professeurs)(_.idProf)
    def matiere = foreignKey("FK_MATIERE", codeMatiereEmploi, matieres)(_.codeMatiere)
    def * = (idEmploi, numeroSalleEmploi, idProfEmploi, codeMatiereEmploi,
      dateEmploi, heureDebut, heureFin, niveau) <> (EmploiTemps.tupled, EmploiTemps.unapply)
  }
  val emplois = TableQuery[Emplois]

  class Etudiants(tag: Tag) extends Table[Etudiant](tag, "ETUDIANT"){
    def idEtud = column[Int]("ID_ETUDIANT", O.PrimaryKey, O.AutoInc)
    def nomEtud = column[String]("NOM_ETUDIANT")
    def prenomEtud = column[String]("PRENOM_ETUDIANT")
    def niveauEtude = column[String]("NIVEAU_ETUDE")
    def * = (idEtud, nomEtud, prenomEtud,
      niveauEtude) <> (Etudiant.tupled, Etudiant.unapply)
  }
  val etudiants = TableQuery[Etudiants]

  class EtudiantMatieres(tag: Tag) extends Table[EtudiantMatiere](tag, "ETUDIANT_MATIERE"){
    def idEtudMat = column[Int]("ID_ETUDIANT_MAT")
    def codeMatiereEtud = column[String]("CODE_MATIERE_ETUD")
    def devoir1 = column[Double]("DEVOIR_1")
    def devoir2 = column[Double]("DEVOIR_2")
    def examen = column[Double]("EXAMEN")
    def etudiant = foreignKey("FK_ETUDAINT", idEtudMat, etudiants)(_.idEtud)
    def matiereEtudiant = foreignKey("FK_MATIERE_ETUDIANT", codeMatiereEtud, matieres)(_.codeMatiere)
    def * = (idEtudMat, codeMatiereEtud, devoir1, devoir2,
      examen) <> (EtudiantMatiere.tupled, EtudiantMatiere.unapply)
  }
  val etudiantMatieres = TableQuery[EtudiantMatieres]

  class  Admins(tag: Tag) extends Table[Admin](tag, "ADMIN"){
    def idAdmin = column[Int]("ID_ADMIN", O.PrimaryKey, O.AutoInc)
    def login = column[String]("LOGIN", O.Unique)
    def motDePasse = column[String]("MOT_DE_PASSE")
    def nomAdmin = column[String]("NOM_ADMIN")
    def prenomAdmin = column[String]("PRENOM_ADMIN")
    def dateAjout = column[String]("DATE_AJOUT")
    def dateSuppression = column[String]("DATE_SUPPRESSION")
    def * = (idAdmin, login, motDePasse, nomAdmin,
      prenomAdmin, dateAjout, dateSuppression) <> (Admin.tupled, Admin.unapply)
  }
  val admins = TableQuery[Admins]
  val tousLesSchemas = matieres.schema ++ professeurs.schema ++ salles.schema ++ emplois.schema ++
    etudiants.schema ++ etudiantMatieres.schema ++ admins.schema

}
