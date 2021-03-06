FactoryGirl.define do

  sequence(:email) { |n| "admin#{n}@example.com" }

  factory :admin_user do
    email
    password 'supersecret'
  end

  factory :delayed_job, class: Delayed::Job do
    handler 'hello'
  end

  factory :setting do
  end

  factory :metric do
  end

  # TODO delete me, as soon as metrics are established
  factory :social_share do
  end

  factory :user do
    firstname "Max"
    lastname "Mustermann"
    
    email
  end

  factory :series do
    title "A title"
    teaser "A teaser"
    description "A description"
    
    user
  end

  factory :venue do
    user
    name 'Some name'
  end

  sequence(:uri) { |n| "uri-#{n}" }

  factory :talk do
    uri 'sc15-123'
    title "Some awesome title"
    series
    venue
    # NOTE: times set here are not affected by `Timecop.freeze` in a
    # `before` block
    starts_at 1.hour.from_now
    duration 60
    tag_list 'lorem, ipsum, dolor'
    description 'Some talk description'
    language 'en'
  end

end
