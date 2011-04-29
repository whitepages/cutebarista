class Rating < ActiveRecord::Base
  LONGITUDE_RANGE = -180.0..180.0
  LATITUDE_RANGE  = -90.0..90.0
  validates_presence_of :lat, :lon, :rating
  validates_numericality_of :lat, :lon
  validates_numericality_of :rating, :integer_only => true
  validates_inclusion_of :lon, :in => LONGITUDE_RANGE, :msg => "longitude out of range #{LONGITUDE_RANGE}"
  validates_inclusion_of :lat, :in => LATITUDE_RANGE,  :msg => "latitude out of range #{LATITUDE_RANGE}"
  
  before_validation(:on => :create) do
    self.dt = Time.now
  end
end
