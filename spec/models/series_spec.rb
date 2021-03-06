require 'rails_helper'

RSpec.describe Series, type: :model do

  it 'updates description_html when updating the description' do
    s = FactoryGirl.create :series
    expect(s.description_as_html).to eq("<p>A description</p>\n")
    s.update_attribute :description, "test description list:\n* item 1\n* item 2"
    expect(s.description_as_html).to_not eq("<p>A description</p>\n")
  end

end
