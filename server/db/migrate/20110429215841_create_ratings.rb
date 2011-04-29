class CreateRatings < ActiveRecord::Migration
  def self.up
    create_table :ratings do |t|
      t.datetime :dt
      t.float :lat
      t.float :lon
      t.string :device_id
      t.integer :rating
    end
  end

  def self.down
    drop_table :ratings
  end
end
