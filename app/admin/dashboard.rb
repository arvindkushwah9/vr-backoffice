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

  title = 'With Great Power Comes Great Responsibility.'
  content title: title do
    div id: 'livedashboard', style: 'height: 100vh' do
      script do
        x = <<-EOF

        fayeUrl = '#{Settings.faye.server}';

        fayeClient = new Faye.Client(fayeUrl);
        fayeExtension = new FayeAuthentication(fayeClient);
        fayeClient.addExtension(fayeExtension);

        mappings = {
          devices: #{Device.mapping.to_json}
        }

        briefings = {
          servers: #{EC2.briefing.to_json},
          venues: #{Venue.briefing.to_json},
          talks: #{Talk.briefing.to_json}
        }

        EOF
        x.html_safe
      end
    end
    script src: '/js/app.js'
  end
end
