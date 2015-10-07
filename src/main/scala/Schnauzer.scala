// package schnauzer
//
// object Schnauzer {
//   type EpochTime = Long
//   type Reply = Task[Bytes]
//   type Sort = List[SortSpec]
//   type Cache = Boolean
//   type PrefixValue = String
//
//   def defaultCache: Cache = false
//
//   type ShardCount = Int
//   type ReplicaCount = Int
//   type Server = String
//   type IndexName = String
//   type MappingName = String
//   type DocId = String
//   type QueryString = String
//   type FieldName = String
//   type CacheName = String
//   type Existence = Boolean
//   type NullValue = Boolean
//   type CutoffFrequency = Double
//   type Analyzer = String
//   type MapExpansions = Int
//   type Lenient = Boolean
//   type Tiebreaker = Double
//   type Boost = Double
//   type BoostTerms = Double
//   type MinimumMatch = Int
//   type MinimumMatchText = String
//
//   type TrackSortScores = Boolean
//   type From = Int
//   type Size = Int
// }
//
// import Schnauzer._
//
// /** `Version` is embedded in `Status` */
// case class Version(number: String,
//                    buildHash: String,
//                    buildTimestamp: EpochTime,
//                    buildSnapshot: Boolean,
//                    luceneVersion: String)
//
// /**
//  * `Status` is a data type for describing the JSON body returned by
//  * Elasticsearch when you query its status. This was deprecated in 1.2.0
//  * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/indices-status.html#indices-status]]
//  */
// case class Status(ok: Option[Boolean],
//                   status: Int,
//                   name: String,
//                   version: Version,
//                   tagline: String)
//
// /**
//  * `IndexSettings` is used to configure the shards and replicas when you create
//  * an Elasticsearch Index.
//  * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/indices-create-index.html]]
//  */
// case class IndexSettings(indexShards: ShardCount,
//                          indexReplicas: ReplicaCount)
//
// object IndexSettings {
//   /**
//    * An `IndexSettings` with 3 shards and 2 replicas.
//    */
//   lazy val default = IndexSettings(Shardcount(3), ReplicaCount(2))
// }
//
// /** `OpenCloseIndex` is a sum type for opening and closing indices. */
// sealed trait OpenCloseIndex
// case object OpenIndex extends OpenCloseIndex
// case object CloseIndex extends OpenCloseIndex
//
// sealed trait FieldType
// case object GeoPointType extends FieldType
// case object GeoShapeType extends FieldType
// case object FloatType extends FieldType
// case object IntegerType extends FieldType
// case object LongType extends FieldType
// case object ShortType extends FieldType
// case object ByteType extends FieldType
//
// case class FieldDefinition(fieldType: FieldType)
//
// case class MappingField(mappingFieldName: FieldName,
//                         fieldDefinition: FieldDefinition)
//
// /**
//  * Support for type reification of `Mapping`s is currently incomplete, for
//  * now the mapping API verbiage expects a `ToJSON`able blob.
//  * Indexes have mappings, mappings are schemas for the documents contained in the
//  * index. I'd recommend having only one mapping per index, always having a mapping,
//  * and keeping different kinds of documents separated if possible.
//  */
// case class Mapping(typeName: TypeName,
//                    mappingFields: List[MappingField])
//
// /**
//  * `BulkOperation` is a sum type for expressing the four kinds of bulk
//  * operation index, create, delete, and update. `BulkIndex` behaves like an
//  * "upsert", `BulkCreate` will fail if a document already exists at the DocId.
//  * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/docs-bulk.html#docs-bulk]]
// */
// sealed trait BulkOperation
// case class BulkIndex(indexName: IndexName, mappingName: MappingName, docId: DocId, value: Value)
// case class BulkCreate(indexName: IndexName, mappingName: MappingName, docId: DocId, value: Value)
// case class BulkDelete(indexName: IndexName, mappingName: MappingName, docId: DocId)
// case class BulkUpdate(indexName: IndexName, mappingName: MappingName, docId: DocId, value: Value)
//
// /**
//  * `EsResult` describes the standard wrapper JSON document that you see in
//  * successful Elasticsearch responses.
//  */
// case class EsResult[A](_index: String,
//                        _type: String,
//                        _id: String,
//                        _version: Int,
//                        found: Option[Boolean],
//                        _source: A)
//
// /**
//  * The two main kinds of `SortSpec` are `DefaultSortSpec` and
//  * `GeoDistanceSortSpec`. The latter takes a `SortOrder`, `GeoPoint`, and
//  * `DistanceUnit` to express "nearness" to a single geographical point as a
//  * sort specification.
//  * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#search-request-sort]]
// */
// sealed trait SortSpec
// case class DefaultSortSpec(default: DefaultSort) extends SortSpec
// case class GeoDistanceSortSpec(sortOrder: SortOrder,
//                                geoPoint: GeoPoint,
//                                distanceUnit: DistanceUnit) extends SortSpec
//
// /**
//  * `DefaultSort` is usually the kind of `SortSpec` you`ll want.
//  * [[<http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#search-request-sort>]]
// */
// case class DefaultSort(sortFieldName: FieldName,
//                        sortOrder: SortOrder,
//                        ignoreUnmapped: Boolean = false,
//                        sortMode: Option[SortMode] = None,
//                        missingSort: Option[Missing] = None,
//                        nestedFilter: Option[Filter] = None)
//
// /**
//  * `SortOrder` is `Ascending` or `Descending`, as you might expect. These get
//  * encoded into "asc" or "desc" when turned into JSON.
//  * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#search-request-sort]]
// */
// sealed trait SortOrder
// case object Ascending extends SortOrder
// case object Descending extends SortOrder
//
// /**
//  * 'Missing' prescribes how to handle missing fields. A missing field can be
//  * sorted last, first, or using a custom value as a substitute.
//  * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#_missing_values]]
//  */
// sealed trait Missing
// case object LastMissing extends Missing
// case object FirstMissing extends Missing
// case class CustomMissing(value: String) extends Missing
//
// /**
//  * 'SortMode' prescribes how to handle sorting array/multi-valued fields.
//  * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#_sort_mode_option]]
//  */
// sealed trait SortMode
// case object SortMin extends SortMode
// case object SortMax extends SortMode
// case object SortSum extends SortMode
// case object SortAvg extends SortMode
//
// sealed trait BooleanOperator
// case object And extends BooleanOperator
// case object Or extends BooleanOperator
//
// case class Search(queryBody: Option[Query],
//                   filterBody: Option[Filter],
//                   sortBody: Option[Sort],
//                   aggBody: Option[Aggregations],
//                   highlight: Option[Highlights],
//                   trackSortScores: TrackSortScores = false,
//                   from: From,
//                   size: Size)
//
// case class Highlights(globalSettings: Option[HighlightSettings],
//                       highlightFields: List[FieldHighlight])
//
// case class FieldHighlight(fieldName: FieldName)
//
// sealed trait HighlightSettings
// case class Plain(highlight: PlainHighlight) extends HighlightSettings
// case class Postings(highlight: PostingsHighlight) extends HighlightSettings
// case class FastVector(highlight: FasctVectorHighlight) extends HighlightSettings
//
// case class PlainHighlight(plainCommon: Option[CommonHighlight],
//                           plainNonPost: Option[NonPostings])
// case class PostingsHighlight(postingsCommon: Option[CommonHighlight])
// case class FastVectorHighlight(fvCommon: Option[CommonHighlight],
//                                fvNonPostSettings: Option[NonPostings],
//                                boundaryChars: Option[String],
//                                boundaryMaxScan: Option[Int],
//                                fragmentOffset: Option[Int],
//                                matchedFields: List[String],
//                                phraseLimit: Option[Int])
//
// case class CommonHighlight(order: Option[String],
//                            forceSource: Option[Boolean],
//                            tag: Option[HighlightTag],
//                            encoder: Option[HighlightEncoder],
//                            noMatchSize: Option[Int],
//                            highlightQuery: Option[Query],
//                            requreFieldMatch: Option[Boolean])
//
// case class NonPostings(fragmentSize: Option[Int],
//                        numberOfFragments: Option[Int])
//
// sealed trait HighlightEncoder
// case object DefaultEncoder extends HighlightEncoder
// case object HTMLEncoder extends HighlightEncoder
//
// sealed trait HighlightTag
// case class TagSchema(tag: String) extends HighlightTag
// case class CustomTags(tags: (List[String], List[String])) extends HighlightTag
//
//
