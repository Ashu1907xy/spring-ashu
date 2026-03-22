package spring_ashu.spring_ashu;



import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;


@Configuration
public class MongoConfig {

    @Bean
    public GridFsTemplate gridFsTemplate(MongoDatabaseFactory dbFactory,
                                         MappingMongoConverter converter) {
        return new GridFsTemplate(dbFactory, converter);
    }
}