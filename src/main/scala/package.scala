package object schnauzer {
  type TrackSortScores = Boolean

  /**
   * `Sort` is a synonym for a list of `SortSpec`s. Sort behaviour is order
   * dependent with later sorts acting as tie-breakers for earlier sorts.
   */
  type Sort = List[SortSpec]

  type From = Int
  type To = Int
  type Size = Int

  /**
   * `MinimumMatch` controls how many should clauses in the bool query should
   * match. Can be an absolute value (2) or a percentage (30%) or a
   * combination of both.
   */
  type MinimumMatch = Int

  type FieldName = String

  // Caching on/off
  type Cache = Boolean

  type LuceneQuery = String

  /**
   * `MappingName` is part of mappings which are how ES describes and schematizes
   * the data in the indices.
   */
  type MappingName = String

  /**
   * `DocId` is a generic wrapper value for expressing unique Document IDs.
   * Can be set by the user or created by ES itself. Often used in client
   * functions for poking at specific documents.
   */
  type DocId = String

  /**
   * `IndexName` is used to describe which index to query/create/delete.
   */
  type IndexName = String

  // Double range types
  type LessThan = Double
  type LessThanEq = Double
  type GreaterThan = Double
  type GreaterThanEq = Double

  // UTC DateTime range types
  type LessThanD = Long
  type LessThanEqD = Long
  type GreaterThanD = Long
  type GreaterThanEqD = Long
}

