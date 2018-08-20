package postgersqlsequence.demo

import kotlinx.coroutines.experimental.launch
import org.hibernate.annotations.GenericGenerator
import org.hibernate.annotations.Parameter
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.repository.CrudRepository
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.EnableTransactionManagement
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.concurrent.TimeUnit
import javax.persistence.*
import javax.persistence.GenerationType.*

@SpringBootApplication
@EnableTransactionManagement
@EnableScheduling
class DemoApplication

fun main(args: Array<String>) {
    runApplication<DemoApplication>(*args)
    Thread.sleep(500)
}


@Service
class Service1( val repository1: Class1Repository){
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun save(i:Int,j:Int) {
        repository1.save(Class1(data = "some data 1.$i.$j"))
    }
}

@Service
class Service2( val repository2: Class2Repository){
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun save(i:Int,j:Int) {
        repository2.save(Class2(data = "some data 1.$i.$j"))
    }
}

@Service
class Service(
        val service1: Service1,
        val service2: Service2
) {
    @Scheduled(initialDelay = 1000, fixedRate = 100_000_000)
    fun init() {
        println("============== START ============== ")
        (1..100).forEach { i ->
            (1..100).forEach { j ->
                launch {
                    service1.save(i,j)
                }
            }

//            (1..100).forEach { j ->
//                launch {
//                    service2.save(i,j)                }
//            }

//        (1..100).forEach { j ->
//            launch {
//                repository3.save(Class3(data = "some data 3.$i.$j"))
//            }
//        }
//
//
//        (1..100).forEach { j ->
//            launch {
//                repository4.save(Class4(data = "some data 4.$i.$j"))
//            }
//        }
        }

        TimeUnit.SECONDS.sleep(10)
    }
}

@Entity
@Table(name = "c1")
data class Class1(
        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = "gen1")
        @GenericGenerator(
                name = "gen1",
                strategy = "sequence",
                parameters = [Parameter(value = "sequence",name = "sequence")]
        )
//        @SequenceGenerator(name = "gen1", sequenceName = "seq1", initialValue = 1)
        val id: Long? = null,
        val data: String
)

@Entity
@Table(name = "c2")
data class Class2(
        @Id
        @GeneratedValue(strategy = SEQUENCE, generator = "gen2")
        @SequenceGenerator(name = "gen2", sequenceName = "seq2", initialValue = 1)
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

@Transactional
interface Class1Repository : CrudRepository<Class1, Long>
@Transactional
interface Class2Repository : CrudRepository<Class2, Long>

interface Class3Repository : CrudRepository<Class3, Long>
interface Class4Repository : CrudRepository<Class4, Long>