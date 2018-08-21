package postgersqlsequence.demo

import kotlinx.coroutines.experimental.launch
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import javax.persistence.*
import javax.persistence.GenerationType.SEQUENCE

@SpringBootApplication
class DemoApplication


// https://ntsim.uk/posts/how-to-use-hibernate-identifier-sequence-generators-properly/
// http://www.postgresqltutorial.com/postgresql-serial/
// https://vladmihalcea.com/why-you-should-never-use-the-table-identifier-generator-with-jpa-and-hibernate/
// https://www.postgresql.org/docs/9.5/static/sql-createsequence.html

fun main(args: Array<String>) {
    val ctx = runApplication<DemoApplication>(*args)
    println("============== START ============== ")
    (1..100).forEach { i ->
        (1..100).forEach { j ->
            launch {
                ctx.getBean(Class1Repository::class.java).save(Class1(data = "some data $i$j"))
            }
        }
        (1..100).forEach { j ->
            launch {
                ctx.getBean(Class2Repository::class.java).save(Class2(data = "some data $i$j"))
            }
        }
    }
    Thread.sleep(1000)
}

@Entity
@Table(name = "c1")
data class Class1(
        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = "gen1")
        @SequenceGenerator(name = "gen1", sequenceName = "seq1", allocationSize = 50)
        val id: Long? = null,
        val data: String
)

@Entity
@Table(name = "c2")
data class Class2(
        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = "gen2")
        @SequenceGenerator(name = "gen2", sequenceName = "seq2", allocationSize = 50)
        val id: Long? = null,
        val data: String
)

@Entity
@Table(name = "c3")
data class Class3(
        @Id
        @GeneratedValue(strategy = SEQUENCE)
        val id: Long? = null,
        val data: String
)

@Entity
@Table(name = "c4")
data class Class4(
        @Id
        @GeneratedValue(strategy = SEQUENCE)
        val id: Long? = null,
        val data: String
)

interface Class1Repository : CrudRepository<Class1, Long>
interface Class2Repository : CrudRepository<Class2, Long>
interface Class3Repository : CrudRepository<Class3, Long>
interface Class4Repository : CrudRepository<Class4, Long>