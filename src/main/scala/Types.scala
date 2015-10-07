package schnauzer

import scalaz.NonEmptyList

case class Search(queryBody: Option[Query],
                  filterBody: Option[Filter],
                  sortBody: Option[Sort],
                  aggBody: Option[Aggs],
                  trackSortScores: TrackSortScores = false,
                  from: From,
                  size: Size,
                  searchType: SearchType,
                  fields: Option[FieldName])

sealed trait Query
case class TermQuery(term: Term) extends Query
case class TermsQuery(terms: NonEmptyList[Term]) extends Query
case class QueryMatchQuery(query: MatchQuery) extends Query
case class QueryBoolQuery(query: BoolQuery) extends Query
case class QueryFilteredQuery(query: FilteredQuery) extends Query
case class IdsQuery(mapping: MappingName, ids: List[DocId]) extends Query
case class QueryIndicesQuery(query: IndicesQuery) extends Query
case class QueryQueryStringQuery(query: QueryStringQuery) extends Query
case class QueryRangeQuery(query: RangeQuery) extends Query

sealed trait Filter
case class AndFilter(filters: List[Filter], cache: Cache) extends Filter
case class OrFilter(filters: List[Filter], cache: Cache) extends Filter
case class NotFilter(filter: Filter, cache: Cache) extends Filter
case object IdentityFilter extends Filter
case class BoolFilter(matches: BoolMatch) extends Filter
case class ExistsFilter(field: FieldName) extends Filter // always cached
case class IdsFilter(mapping: MappingName, ids: List[DocId]) extends Filter
case class LimitFilter(limit: Int) extends Filter
case class QueryFilter(query: Query, cache: Cache) extends Filter
case class RangeFilter(field: FieldName,
                       range: RangeValue,
                       exec: RangeExecution,
                       cache: Cache) extends Filter
case class TermFilter(term: Term, cache: Cache) extends Filter

/**
 * Currently the only type of `SortSpec` we support is `DefaultSortSpec`.
 * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#search-request-sort]]
*/
sealed trait SortSpec
case class DefaultSortSpec(default: DefaultSort) extends SortSpec

/**
 * `DefaultSort` is usually the kind of `SortSpec` you`ll want.
 * [[<http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#search-request-sort>]]
*/
case class DefaultSort(sortFieldName: FieldName,
                       sortOrder: SortOrder,
                       ignoreUnmapped: Boolean = false,
                       sortMode: Option[SortMode] = None,
                       missingSort: Option[Missing] = None,
                       nestedFilter: Option[Filter] = None)

/**
 * `SortOrder` is `Ascending` or `Descending`, as you might expect. These get
 * encoded into "asc" or "desc" when turned into JSON.
 * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#search-request-sort]]
*/
sealed trait SortOrder
case object Ascending extends SortOrder
case object Descending extends SortOrder

/**
 * 'Missing' prescribes how to handle missing fields. A missing field can be
 * sorted last, first, or using a custom value as a substitute.
 * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#_missing_values]]
 */
sealed trait Missing
case object LastMissing extends Missing
case object FirstMissing extends Missing
case class CustomMissing(value: String) extends Missing

/**
 * 'SortMode' prescribes how to handle sorting array/multi-valued fields.
 * [[http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/search-request-sort.html#_sort_mode_option]]
 */
sealed trait SortMode
case object SortMin extends SortMode
case object SortMax extends SortMode
case object SortSum extends SortMode
case object SortAvg extends SortMode

sealed trait BooleanOperator
case object And extends BooleanOperator
case object Or extends BooleanOperator

sealed trait SearchType
case object SearchTypeQueryThenFetch extends SearchType
case object SearchTypeDfsQueryThenFetch extends SearchType
case object SearchTypeCount extends SearchType
case object SearchTypeScan extends SearchType
case object SearchTypeQueryAndFetch extends SearchType
case object SearchTypeDfsQueryAndFetch extends SearchType

case class Term(field: String, value: String)

case class MatchQuery(
  field: FieldName,
  queryString: LuceneQuery,
  operator: BooleanOperator)

case class BoolQuery(
  mustMatch: List[Query],
  mustNotMatch: List[Query],
  shouldMatch: List[Query],
  minimumShouldMatch: Option[MinimumMatch] = None
)

case class FilteredQuery(query: Query, filter: Filter)

case class IndicesQuery(indices: List[IndexName], query: Query, noMatch: Option[Query])

case class QueryStringQuery(
  queryString: LuceneQuery,
  defaultField: Option[FieldName] = None,
  operator: Option[BooleanOperator] = None
)

case class RangeQuery(field: FieldName, range: RangeValue)

sealed trait BoolMatch
case class MustMatch(term: Term, cache: Cache) extends BoolMatch
case class MustNotMatch(term: Term, cache: Cache) extends BoolMatch
case class ShouldMatch(terms: List[Term], cache: Cache) extends BoolMatch

sealed trait RangeValue
case class RangeDateLte(lte: LessThanEqD) extends RangeValue
case class RangeDateLt(lt: LessThanD) extends RangeValue
case class RangeDateGte(gte: GreaterThanEqD) extends RangeValue
case class RangeDateGt(gt: GreaterThanD) extends RangeValue
case class RangeDateGtLt(gt: GreaterThanD, lt: LessThanD) extends RangeValue
case class RangeDateGteLte(gte: GreaterThanEqD, lte: LessThanEqD) extends RangeValue
case class RangeDateGteLt(gte: GreaterThanEqD, lt: LessThanD) extends RangeValue
case class RangeDateGtLte(gt: GreaterThanD, lt: LessThanEqD) extends RangeValue
case class RangeDoubleLte(lte: LessThanEq) extends RangeValue
case class RangeDoubleLt(lt: LessThan) extends RangeValue
case class RangeDoubleGte(gte: GreaterThanEq) extends RangeValue
case class RangeDoubleGt(gt: GreaterThan) extends RangeValue
case class RangeDoubleGtLt(gt: GreaterThan, lt: LessThan) extends RangeValue
case class RangeDoubleGteLte(gte: GreaterThanEq, lte: LessThanEq) extends RangeValue
case class RangeDoubleGteLt(gte: GreaterThanEq, lt: LessThan) extends RangeValue
case class RangeDoubleGtLte(gt: GreaterThan, lte: LessThanEq) extends RangeValue

sealed trait RangeExecution
case object RangeExecutionIndex extends RangeExecution
case object RangeExecutionFieldData extends RangeExecution

