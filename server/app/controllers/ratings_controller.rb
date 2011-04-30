class RatingsController < ApplicationController

  Q_BOUNDS = { # maps param arguments to query modifier methods
    :top    => :south_of,
    :bottom => :north_of,
    :left   => :east_of,
    :right  => :west_of,
    :max    => :limit,
  }
  def index
    @q = Rating
    Q_BOUNDS.each_pair { |p,m| @q = @q.send(m,params[p]) if params[p] }
    @ratings = @q.all
    @entries = @ratings.map { |r| map_attrs_out(r) }
    @result = { :entries => @entries }
    render :json => @result
  end

  ATTRS_IN = [:dt, :lat, :lon, :device_id, :rating]
  def create
    @new_rating = Rating.new do |r|
      ATTRS_IN.each {|a| r.send("#{a}=", params[a]) }
    end
    @new_rating.save!
    render :json => { :id => @new_rating.id }
  end

  ATTRS_OUT_SIMPLE = [ :dt, :lat, :lon, :rating ]
  private
  def map_attrs_out(r)
    out = ATTRS_OUT_SIMPLE.inject({}) do |m,k|
      m[k] = r.send(k)
      m
    end
    out[:me] = (!params[:device_id].blank? && (r.device_id == params[:device_id])) ? 1 : 0
    out
  end

end
