package data

import model.{Entity, EntityId, Version}
import org.joda.time.DateTime

case class TestEntityId(value: Long) extends EntityId(value)

case class TestEntity(id: Option[TestEntityId], version: Option[Version], created: Option[DateTime], modified: Option[DateTime],
                      name: String) extends Entity[TestEntityId]

trait TestEntityComponent extends EntityComponent { this: Profile =>

  import profile.api._

  class TestEntities(tag: Tag) extends Table[TestEntity](tag, "test_entities") with EntityTable[TestEntityId, TestEntity] {
    def id = column[TestEntityId]("id", O.PrimaryKey, O.AutoInc)
    def version = column[Version]("version")
    def created = column[DateTime]("created")
    def modified = column[DateTime]("modified")
    def name = column[String]("name")

    def * = (id.?, version.?, created.?, modified.?, name) <> (TestEntity.tupled, TestEntity.unapply)
  }

  val testEntities = TableQuery[TestEntities]

  object TestEntities extends EntityQueries(testEntities) {

    def copyEntityFields(entity: TestEntity, id: Option[TestEntityId], version: Option[Version], created: Option[DateTime], modified: Option[DateTime]): TestEntity =
      entity.copy(id = id, version = version, created = created, modified = modified)

  }
}
