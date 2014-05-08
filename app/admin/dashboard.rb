ActiveAdmin.register_page "Dashboard" do

  menu :priority => 1, :label => proc{ I18n.t("active_admin.dashboard") }

  content :title => proc{ I18n.t("active_admin.dashboard") } do

    div style: 'font-size: 32px; text-align: center' do
      "WITH GREAT POWER COMES GREAT RESPONSIBILITY"
    end

    # div do
    #   script do
    #     # talks = Talk.prelive + Talk.live
    #     talks = Talk.prelive
    #     raw 'window.talks = ' +
    #         talks.inject({}) { |r, t| r.merge t.id => t.attributes }.to_json
    #   end
    # end
    
    div do
      script do
        raw 'window.talks = ' + Talk.live.map(&:attributes).to_json
      end
    end

    div id: 'notifications', style: 'margin: 30px' do
      subscribe_to "/notifications"
    end
    
    div id: 'livedashboard', style: 'margin: 30px' do
      subscribe_to "/monitoring"
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
