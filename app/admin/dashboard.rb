ActiveAdmin.register_page "Dashboard" do

  menu priority: 0, label: proc{ I18n.t("active_admin.dashboard") }

  page_action :seed do
    render json: {
      talks:            Talk.in_dashboard.map(&:attributes),
      djAudioQueueSize: Delayed::Job.audio.queued.count,
      postliveCount:    Talk.postlive.count,
      streams:          Talk.live.map(&:streams).flatten
    }
  end

  title = 'WITH GREAT POWER COMES GREAT RESPONSIBILITY'
  content title: title do

    # table style: 'width: 200px' do
    #   tr do
    #     th 'Venue State'
    #     th 'Count'
    #   end
    #   Venue.group(:state).count(:state).each do |state, count|
    #     tr do
    #       td state
    #       td count
    #     end
    #   end
    # end

    div id: 'livedashboard', style: 'margin: 30px; height: 100%' do
      script do
        x = <<-EOF
        document.fayeUrl = '#{Settings.faye.server}';
        identifiers = #{Hash[Device.all.pluck(:id, :identifier)].to_json};
        EOF
        x.html_safe
      end
    end

    # div :class => "blank_slate_container", :id => "dashboard_default_message" do
    #   span :class => "blank_slate" do
    #     span I18n.t("active_admin.dashboard_welcome.welcome")
    #     small I18n.t("active_admin.dashboard_welcome.call_to_action")
    #   end
    # end

    # Here is an example of a simple dashboard with columns and panels.
    #
    # columns do
    #   column do
    #     panel "Recent Posts" do
    #       ul do
    #         Post.recent(5).map do |post|
    #           li link_to(post.title, admin_post_path(post))
    #         end
    #       end
    #     end
    #   end

    #   column do
    #     panel "Info" do
    #       para "Welcome to ActiveAdmin."
    #     end
    #   end
    # end
  end # content
end
