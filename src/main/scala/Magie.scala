import slick.jdbc.H2Profile.api._
import slick.jdbc.meta.MTable
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration.Duration
import scala.concurrent.{Await, Future}
import scala.util.Success

trait Magie {
  self: SchemaBD =>

  def db: Database

  def afficherResultats[T](f: Future[Iterable[T]]): Unit = {
    Await.result(f, Duration.Inf).foreach(println)
    println()
  }

  def createSchemaIfNotExists: Future[Unit] ={
    db.run(MTable.getTables).flatMap(tables =>
      if(tables.isEmpty) {
        db.run(tousLesSchemas.create).andThen {
          case Success(_) => println("Schéma créé !1")
        }
      }
      else{
        println("Ce schéma existe déjà !")
        Future.successful()
      })
  }

}
