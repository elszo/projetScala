import caseClasses._
import slick.jdbc.H2Profile.api._

import java.time.LocalDateTime
import scala.collection.mutable.ListBuffer
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}

class AccesDonnees(db: Database) extends  SchemaBD {

// CRUD pour la classe Professeur **************************************************************************

  def ajouterProfesseur(prof: Professeur): Future[Int] = {
    val query = professeurs += prof
    db.run(query)
  }
  def supprimerProfesseur(iden: Int): Future[Unit] = {
    val req = DBIO.seq(emplois.filter(_.idProfEmploi===iden).delete,
      professeurs.filter(_.idProf===iden).delete)
    db.run(req)
  }
  def modifierProfesseur(prof: Professeur): Future[Int] = {
    val modif = professeurs.filter(_.idProf === prof.idProf)
    db.run(modif.update(prof))
  }
  def tousProfesseurs: Future[Seq[Professeur]] = db.run(professeurs.result)


// CRUD pour la classe Salle ***************************************************************************

  def ajouterSalle(sal: Salle): Future[Int] = {
    //val salleID = salles returning salles.map(_.numeroSalle) into ((_, newID) => sal.copy(numeroSalle = newID))
    val query = salles += sal
    db.run(query)
  }
  def supprimerSalle(iden: Int): Future[Int] = db.run(salles.filter(_.numeroSalle===iden).delete)
  def modifierSalle(sal: Salle): Future[Int] = {
    val modif = salles.filter(_.numeroSalle === sal.numeroSalle)
    db.run(modif.update(sal))
  }
  def rechercheSalleLibre(jour: String, debut: String, fin: String): Future[Seq[(Int, String)]] = {
    val num = for{
      emp <- emplois.filter(_.dateEmploi===jour).filter(_.heureDebut===debut).filter(_.heureFin===fin)
    }yield emp.numeroSalleEmploi
    val query = for{
      sal <- salles if ! (sal.numeroSalle in num)
    }yield (sal.numeroSalle, sal.libelleSalle)
    db.run(query.result)
  }
  def salleOccuper: Future[Seq[(Int, String, String, String)]] = {
    var jour: String = LocalDateTime.now().getDayOfWeek.toString
    var heure: String = LocalDateTime.now().getHour.toString
    heure += "h00"

    if (jour == "MONDAY") jour = "Lundi"
    else if (jour == "TUESDAY") jour = "Mardi"
    else if (jour == "WEDNESDAY") jour = "Mercredi"
    else if (jour == "THURSDAY") jour = "Jeudi"
    else if (jour == "FRIDAY") jour = "Vendredi"
    else if (jour == "SATURDAY") jour = "Samedi"
    else jour = "Dimanche"

    val query = for{
      emp <- emplois.filter(_.dateEmploi===jour).filter(_.heureDebut<=heure).filter(_.heureFin>heure)
      sal <- emp.salle
      prof <- emp.professeur
      mat <- emp.matiere
    }yield (sal.numeroSalle, sal.libelleSalle, prof.prenomProf++" "++prof.nomProf, mat.libelleMatiere)
    db.run(query.result)
  }
  def toutesSalles: Future[Seq[Salle]] = db.run(salles.result)


// CRUD pour la classe Matière *************************************************************************

  def ajouterMatiere(mat: Matiere): Future[Int] = {
    val query = matieres += mat
    db.run(query)
  }
  def supprimerMatiere(iden: String): Future[Int] = db.run(matieres.filter(_.codeMatiere===iden).delete)
  def modifierMatiere(mat: Matiere): Future[Int] = {
    val modif = matieres.filter(_.codeMatiere === mat.codeMatiere)
    db.run(modif.update(mat))
  }
  def toutesMatieres: Future[Seq[Matiere]] = db.run(matieres.result)


// CRUD pour la classe EmploiTemps *******************************************************************

  def ajouterEmploiTemps(emp: EmploiTemps): Future[Int] = {
    val query = emplois += emp
    db.run(query)
  }
  def supprimerEmploiTemps(iden: Int): Future[Int] = db.run(emplois.filter(_.idEmploi===iden).delete)
  def modifierEmploiTemps(emp: EmploiTemps): Future[Int] = {
    val modif = emplois.filter(_.idEmploi === emp.idEmploi)
    db.run(modif.update(emp))
  }
  def afficherEmploiTemps: Future[Seq[EmploiTemps]] = db.run(emplois.result)

  def afficherEmploiTempsParClasse(classe : String) = {
    val cl = emplois.filter(_.niveau===classe)
    val lundi = cl.filter(_.dateEmploi==="Lundi")
    val mardi = cl.filter(_.dateEmploi==="Mardi")
    val mercredi = cl.filter(_.dateEmploi==="Mercredi")
    val jeudi = cl.filter(_.dateEmploi==="Jeudi")
    val vendredi = cl.filter(_.dateEmploi==="Vendredi")
    val samedi = cl.filter(_.dateEmploi==="Samedi")

    val lu = db.run(lundi.result)
    val mar = db.run(mardi.result)
    val mer = db.run(mercredi.result)
    val jeu = db.run(jeudi.result)
    val ve = db.run(vendredi.result)
    val sa = db.run(samedi.result)
    (lu, mar, mer, jeu, ve, sa)

  }


// CRUD pour la classe Etudiant *************************************************************************

  def ajouterEtudiant(etud: Etudiant): Future[Int] = {
    val query = etudiants += etud
    db.run(query)
  }
  def supprimerEtudiant(iden: Int): Future[Int] = db.run(etudiants.filter(_.idEtud===iden).delete)
  def modifierEtudiant(etud: Etudiant): Future[Int] = {
    val modif = etudiants.filter(_.idEtud === etud.idEtud)
    db.run(modif.update(etud))
  }
  def listerNotesEtudiant(iden: Int): Future[Seq[(String, String, Double, Double, Double, Int)]] = {
    val query = for{
      etmat <- etudiantMatieres.filter(_.idEtudMat === iden)
      et <- etmat.etudiant
      mat <- etmat.matiereEtudiant
    }yield (et.prenomEtud++" "++et.nomEtud,etmat.codeMatiereEtud, etmat.devoir1,
      etmat.devoir2, etmat.examen, mat.coefficient)
    db.run(query.result)
  }
  def moyenneEtudiantMatiere(iden: Int): ListBuffer[(String, String, Double, Int)] = {
    val note = Await.result(listerNotesEtudiant(iden), Duration.Inf)
    val moyenneMatiere = new ListBuffer[(String, String, Double, Int)]()
    for(n <- note){
      var moMat = (n._3 + n._4)/2.0
      moMat = (moMat + n._5)/2.0
      moyenneMatiere += Tuple4(n._1, n._2, moMat, n._6)
    }
    moyenneMatiere
  }
  def moyenneGlobaleEtudiant(iden: Int): Double = {
    val moyMat = moyenneEtudiantMatiere(iden)
    var sommeNote: Double = 0.0
    var sommeCoef: Int = 0
    for (n <- moyMat){
      sommeNote += n._3 * n._4
      sommeCoef += n._4
    }

    BigDecimal(sommeNote/sommeCoef).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
  }
  def tousEtudiants: Future[Seq[Etudiant]] = db.run(etudiants.result)


// CRUD pour la classe EtudiantMatiere *******************************************************************

  def ajouterEtudiantMatiere(etMat: EtudiantMatiere): Future[Int] = {
    val query = etudiantMatieres += etMat
    db.run(query)
  }
  def supprimerEtudiantMatiere(iden: Int, code: String): Future[Int] = db.run(etudiantMatieres
    .filter(_.idEtudMat===iden).filter(_.codeMatiereEtud === code).delete)

  def modifierEtudiantMatiere(etMat: EtudiantMatiere): Future[Int] = {
    val modif = etudiantMatieres.filter(_.idEtudMat === etMat.idEtuMat)
      .filter(_.codeMatiereEtud===etMat.codeMatiereEtud)
    db.run(modif.update(etMat))
  }
  def tousEtudiantMatiere: Future[Seq[EtudiantMatiere]] = db.run(etudiantMatieres.result)


// CRUD pour la classe Admin **************************************************************************

  def ajouterAdmin(adm: Admin): Future[Int] = {
    val query = admins += adm
    db.run(query)
  }
  def supprimerAdmin(iden: Int): Future[Int] = db.run(admins.filter(_.idAdmin===iden).delete)
  def modifierAdmin(adm: Admin): Future[Int] = {
    val modif = admins.filter(_.idAdmin === adm.idAdmin)
    db.run(modif.update(adm))
  }
  def tousAdmins: Future[Seq[Admin]] = db.run(admins.result)

// Fonctions qui affichent les statistiques de l'établissement *********************************************

  def moyenneClasse(classe: String): Double = {
    val query = for{
      numero <- etudiants.filter(_.niveauEtude===classe)
    }yield numero.idEtud
    val  req = Await.result(db.run(query.result), Duration.Inf)
    var sommeMoyenne: Double = 0.0
    var nombreEtudiant: Int = 0
    for(q <- req){
      sommeMoyenne += moyenneGlobaleEtudiant(q)
      nombreEtudiant += 1
    }
    BigDecimal(sommeMoyenne/nombreEtudiant).setScale(3, BigDecimal.RoundingMode.HALF_UP).toDouble
  }

  def tauxReussiteEchec: (Double, Double) = {
    val query = for{
      etud <- etudiants
    }yield etud.idEtud
    val  req = Await.result(db.run(query.result), Duration.Inf)
    var nombreReussite: Int = 0
    var nombreEchec: Int = 0
    var nombreEtudiant: Int = 0
    for(q <- req){
      if (moyenneGlobaleEtudiant(q) >= 10.0) nombreReussite += 1
      else nombreEchec += 1
      nombreEtudiant += 1
    }

    val reussite = BigDecimal((nombreReussite*100).toDouble/nombreEtudiant).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble
    val echec = BigDecimal((nombreEchec*100).toDouble/nombreEtudiant).setScale(2, BigDecimal.RoundingMode.HALF_UP).toDouble

    (reussite, echec)
  }

  def meilleursEtudiants:ListBuffer[(Int, String, Double, String)] = {
    val etud = etudiants
    val query = Await.result(db.run(etud.result), Duration.Inf)
    val listeEtud = new ListBuffer[(Int, String, Double, String)]()
    for(q <- query) {
      listeEtud += Tuple4(q.idEtud, q.prenomEtud++" "++q.nomEtud,
        moyenneGlobaleEtudiant(q.idEtud), q.niveauEtude)
    }
    listeEtud.foreach(println)
    val meilleurs = listeEtud.sortBy(_._3)(Ordering[Double].reverse).take(3)
    meilleurs
  }

  def etudiantClasse3: Future[Seq[(String, String)]] = {
    val query = for{ e <- etudiants if e.niveauEtude === "3ième"} yield (e.prenomEtud, e.nomEtud)
    //query.result.statements.foreach(println)
    db.run(query.result)
  }

}
