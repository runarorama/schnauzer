package schnauzer

// A (very) partial specification of the ElasticSearch aggregations API.

sealed trait Interval
case object Year extends Interval
case object Quarter extends Interval
case object Month extends Interval
case object Week extends Interval
case object Day extends Interval
case object Hour extends Interval
case object Minute extends Interval
case object Second extends Interval
case class FractionalInterval(fraction: Double, timeInterval: TimeInterval) extends Interval

sealed trait TimeInterval
case object Weeks extends TimeInterval
case object Days extends TimeInterval
case object Hours extends TimeInterval
case object Minutes extends TimeInterval
case object Seconds extends TimeInterval

case class TermOrder(field: String, order: SortOrder)

case class Aggs(aggs: Map[String, Agg]) {
  def ++(other: Aggs): Aggs = Aggs(aggs ++ other.aggs)
}

sealed abstract class Agg {
  def op: String
}
case class MetricAgg(metric: Metric) extends Agg {
  def op = metric.op
  def field = metric.field
}
case class BucketAgg(bucket: Bucket, aggs: Option[Aggs] = None) extends Agg {
  def op = bucket.op
}

sealed abstract class Bucket(val op: String)
case class TermsAgg(field: String,
                    size: Option[Int] = Some(0),
                    order: Option[TermOrder] = None) extends Bucket("terms")
case class DateHistogram(field: String,
                         interval: Interval,
                         format: Option[String] = None) extends Bucket("date_histogram")
case class FilterAgg(filter: LuceneQuery) extends Bucket("filter")

sealed abstract class Metric(val op: String, val field: String)
case class Stats(override val field: String) extends Metric("stats", field)
case class ExtendedStats(override val field: String) extends Metric("extended_stats", field)
case class Percentiles(override val field: String,
                       percents: Option[List[Double]] = None) extends Metric("percentiles", field)

object Aggs {
  def apply(aggs: (String, Agg)*): Aggs = Aggs(aggs.toMap)
}


