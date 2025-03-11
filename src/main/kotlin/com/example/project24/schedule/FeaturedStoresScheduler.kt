import com.example.project24.store.StoreService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.slf4j.Logger
import org.slf4j.LoggerFactory

@Component
class FeaturedStoresScheduler {

    @Autowired
    lateinit var storeService: StoreService

    private val logger: Logger = LoggerFactory.getLogger(FeaturedStoresScheduler::class.java)

    @Scheduled(cron = "0 * * * * *")
    fun createFeaturedStores() {
        val stores = storeService.getLatestStores().sortedByDescending { it.id }
        logger.info("stores")
    }
}