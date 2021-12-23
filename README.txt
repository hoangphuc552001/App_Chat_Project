Hướng dẫn sử dụng app chat:

B1: Mở file: Server.jar
  + UI bao gồm: 
	- tên Server, số Port, trạng thái(Status) của Server, số lượng user online
	- màn hình thể hiện thông tin user online
	- nút Start, Stop, Exit
  + Người dùng bấm Start để khởi động Server(trạng thái sẽ đổi thành "Running")

B2: Mở file: Client.jar
.Người dùng chọn Register(Đăng Ký)
  + 4 thông tin cần nhập là: name,username,password,password confirm
  + Khi đăng ký thành công sẽ có JOptionPane thông báo => Chuyển qua màn hình đăng nhập
.Người dùng sau khi đăng ký hoặc đã đăng ký rồi thì chọn Log in(Đăng nhập)
  + Người dùng nhập 2 thông tin: username và password đã đăng ký
  + Khi đăng nhập thành công sẽ có JOptionPane thông báo => Chuyển qua màn hình chat
.Màn hình chat
  + UI bao gồm:
	- Màn hình chat
	- Combo box bên trái để người dùng chọn những user đang online để chat
	- List bên phải để người dùng xem được danh sách user đang online
	- Tên của user được thể hiện dưới màn hình
	- Tên của user đang chat được thể hiện trên màn hình
	- Khung chat
	- Nút Send,Upload,Download,Logout
  + Người dùng sử dụng combo box để chọn user đang online để chat:
	- Sau khi chọn, bên phía cả hai sẽ có JOptionPane để thông báo đối phương muốn
	 	chat với bạn
	- Người dùng sau khi nhập tin nhắn ở khung chat thì có thể Nút Send hoặc phím Enter
		để gửi tin nhắn
  + Upload và Download File
	- Người dùng bấm nút Upload thì sẽ hiện ra JFileChooser cho người dùng có thể chọn 
		file để upload, sau khi chọn và gửi thì đồng thời bên phía người nhận sẽ 
		lập tức nhận được thông báo để lưu file, sau đó người nhận chọn vị trí để lưu file
	- Nếu người nhận không muốn download ngay lập tức thì có thể nhấn Cancel, và khi nào
		người nhận file muốn download những file đã nhận thì bấm vào nút Download
		và điền tên file đã được nhận để download
  + Sau khi chat và gửi nhận file xong, người dùng có thể bấm logout để đăng xuất, hoặc bấm nút
		window closing để thoát chương trình. Những user đang online mà đang chat với
		user vừa logout thì sẽ được quay về màn hình chính

B3. Người dùng qua màn hình của Server để Stop Server. Sau đó nhấn nút Exit và bấm Window Closing để đóng Server