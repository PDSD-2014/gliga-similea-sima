class VideogerViewController < UIViewController
  def viewDidLoad
    self.view = UIWebView.alloc.init
    url = NSURL.URLWithString("http://hidden-hamlet-4327.herokuapp.com")
    # url = NSURL.URLWithString("http://192.168.0.103:3000/conversations/new")
    request = NSURLRequest.requestWithURL(url)
    self.view.loadRequest request
  end
end
