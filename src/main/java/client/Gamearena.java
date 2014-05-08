package client;

import client.game.SimpleGameInfo;
import client.game.xo.CellState;
import client.game.xo.GameXO_State;
import client.game.xo.GameXO_WinState;
import client.game.xo.XOCellButton;
import client.gui.OpenGameButton;
import client.person.Person;
import client.services.authorization.AuthorizationService;
import client.services.authorization.AuthorizationServiceAsync;
import client.services.chat.ChatService;
import client.services.chat.ChatServiceAsync;
import client.services.game.GameService;
import client.services.game.GameServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;

import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.json.client.*;
import com.google.gwt.user.client.Cookies;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.*;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Gamearena implements EntryPoint {
    //===== user info =============================
    Boolean isLogin;


    //Label usersOnlineLabel = new Label();
    ArrayList<Person> usersOnline = new ArrayList<Person>();
    Long lastReadChatMsgIndex = 0L;

    String myAccount;



    boolean gameCreated = false; // признак того, что игра создана
    boolean gamePlaying = false; // признак того, что игра сейчас идёт
    Integer gameId; // идентификатор текущей игры
    boolean myAction; // признак своего хода в течении игры
    boolean iAmFirstPlayer; // player_1 = X, player_2 = O
    String gameOpponentAccount;
    boolean gameOver = false;
    GameXO_State curGameXOState = new GameXO_State();

    //############################################################################################################
    //##### GUI WIDGETS ##########################################################################################
    //############################################################################################################

    private Widget BR = new HTML("<br>");

    //===== Login Form =========================================================================================
    private VerticalPanel userVerticalPanel = new VerticalPanel();
    private HorizontalPanel loginButtonsPanel = new HorizontalPanel();
    private FlexTable userTable = new FlexTable();
    private TextBox loginUserTextBox = new TextBox();
    private PasswordTextBox loginPassTextBox = new PasswordTextBox();
    private Button loginButton = new Button("Login");
    private Label userNameLabel = new Label("Name");
    private Label passwordLabel = new Label("Password");
    private Label errorLoginMessage = new Label("");
    private Button newRegistrationButton = new Button("No account? Make it easy :)");

    //===== RegistrationService Form ============================================================================
    private Label accountLabel = new Label("NickName");
    private TextBox accountTB = new TextBox();
    private Label nameLabel = new Label("First name");
    private TextBox nameTB = new TextBox();
    private Label lastNameLabel = new Label("Last name");
    private TextBox lastNameTB = new TextBox();
    private Label passLabel = new Label("Password");
    private PasswordTextBox passTB = new PasswordTextBox();
    private Label confirmPassLabel = new Label("Confirm password");
    private PasswordTextBox confirmPassTB = new PasswordTextBox();
    private Button registrationButton = new Button("Registration");
    private Button backToLoginButton = new Button("Back to Login");
    private Label registrationErrorLabel = new Label();

    //===== Users online form =========================================================================================
    private List<Label> usersOnlineList = new ArrayList<Label>();
    private Label title = new Label();
    private HorizontalPanel usersOnlinePanel = new HorizontalPanel();

    //===== User Statistic Info =======================================================================================
    private VerticalPanel userStatsPanel = new VerticalPanel();
    private Button closeUserStatsPanelBtn = new Button("Close stats");

    //===== GameBoard ============================================================================================
    private VerticalPanel gameboardPanel =  new VerticalPanel();
    private Label gameIdLabel = new Label();
    private Label gameSystemMessage = new Label();
    private FlexTable gameTable = new FlexTable();
    private XOCellButton gameTableButtons[][] = new XOCellButton[3][3];
    private Button backToGameLobbyBtn = new Button("Back to the Lobby.");
    private Button closeGameBtn = new Button("Exit game");
    Boolean closeGameLock =false;

    //===== Game Lobby ===========================================================================================
    List<OpenGameButton> openGames = new ArrayList<OpenGameButton>();
    //HorizontalPanel openGamesPanel = new HorizontalPanel();
    Button createNewGameButton = new Button("Create new XO game");
    Boolean createrNewGameLock =false;
    VerticalPanel createNewGameBtnPanel = new VerticalPanel();
    Label createNewGameLabel = new Label("You may create a new game:");
    Label joinToOpenGamesLabel = new Label("or join to one of open games :");

    //===== Chat =============
    VerticalPanel chatPanel = new VerticalPanel();
    Button sendChatMessageBtn = new Button("Send");
    TextBox chatNewMassageText = new TextBox();
    HorizontalPanel sendNewMessagePanel = new HorizontalPanel();
    VerticalPanel chatMуssages = new VerticalPanel();
    ScrollPanel chatMуssagesScroll = new CustomScrollPanel();

    //############################################################################################################
    //##### INIT WIDGETS FUNCTIONS ###############################################################################
    //############################################################################################################

    // Отображение панели для входа пользователя
    public void showLoginPanel(){
        userVerticalPanel.clear();
        userVerticalPanel.setWidth("100%");
        userVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        loginPassTextBox.addKeyPressHandler(loginPassTextBoxKeyPressHandler);
        userTable.clear();
        loginUserTextBox.setText("");
        loginPassTextBox.setText("");
        userTable.setWidget(0, 0, userNameLabel);
        userTable.setWidget(0,1,loginUserTextBox);
        userTable.setWidget(1,0,passwordLabel);
        userTable.setWidget(1,1,loginPassTextBox);
        userVerticalPanel.add(new HTML("<h3>Login please</h3>"));
        userVerticalPanel.add(userTable);
        loginButton.addClickHandler(loginBtnClickHandler);
        loginButtonsPanel.add(loginButton);
        newRegistrationButton.addClickHandler(newRegistrationBtnClickHandler);
        loginButtonsPanel.add(newRegistrationButton);
        userVerticalPanel.add(loginButtonsPanel);
        userVerticalPanel.add( new HTML("<br>"));
        errorLoginMessage.setStyleName("errorMessage");
        userVerticalPanel.add(errorLoginMessage);
        RootPanel.get("loginPanel").add(userVerticalPanel);
    }

    // Отображение панели регистрации
    public void showRegistrationPanel(){
        userVerticalPanel.clear();
        userVerticalPanel.setWidth("100%");
        userVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        userTable.clear();
        userTable.setWidget(0, 0, accountLabel);
        userTable.setWidget(0,1,accountTB);
        userTable.setWidget(1,0,nameLabel);
        userTable.setWidget(1,1,nameTB);
        userTable.setWidget(2,0,lastNameLabel);
        userTable.setWidget(2,1,lastNameTB);
        userTable.setWidget(3,0,passLabel);
        userTable.setWidget(3,1,passTB);
        userTable.setWidget(4,0,confirmPassLabel);
        userTable.setWidget(4,1,confirmPassTB);
        registrationButton.addClickHandler(registrationBtnClickHandler);
        backToLoginButton.addClickHandler(backToLoginBtnHandler);
        userTable.setWidget(5, 0, registrationButton);
        userTable.setWidget(5, 1, backToLoginButton);
        userVerticalPanel.add(new HTML("<h3>Registration form </h3>"));
        userVerticalPanel.add(userTable);
        registrationErrorLabel.setStyleName("errorMessage");
        userVerticalPanel.add(registrationErrorLabel);
        RootPanel.get("loginPanel").clear();
        RootPanel.get("loginPanel").add(userVerticalPanel);
    }

    public void showWelcomePanel(){
        userVerticalPanel.clear();
        userVerticalPanel.setWidth("100%");
        userVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        Widget userNameWidget = new HTML("Welcome " + myAccount + " !" );
        userNameWidget.setStyleName("boldText");
        userVerticalPanel.add(userNameWidget);
        Button logOut = new Button("Log out");
        logOut.addClickHandler(logoutBtnClickHandler);
        userVerticalPanel.add(logOut);
        RootPanel.get("loginPanel").clear();
        RootPanel.get("loginPanel").add(userVerticalPanel);
    }

    public void showSuccessfulRegistration(String newAccount){
        userVerticalPanel.clear();
        userVerticalPanel.setWidth("100%");
        userVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        Widget userNameWidget = new HTML("Регистрация аккаунта \"" + newAccount + "\" прошла успешно!" );
        userNameWidget.setStyleName("boldText");
        userVerticalPanel.add(userNameWidget);
        backToLoginButton.addClickHandler(backToLoginBtnHandler);
        userVerticalPanel.add(backToLoginButton);
        RootPanel.get("loginPanel").clear();
        RootPanel.get("loginPanel").add(userVerticalPanel);
    }

    void initXOCellButton(XOCellButton btn){
        btn.setSize("25px", "25px");
        btn.addClickHandler(XOGameCellBtnClickHandler);
        btn.setStyleName("ocupyXOCell");
        btn.setEnabled(false);
    }

    void initGameboardWidget(){
        gameboardPanel.setWidth("100%");
        gameboardPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        gameTable.setBorderWidth(1);
        gameIdLabel.setStyleName("boldText");
        gameSystemMessage.setStyleName("boldText");
        closeGameBtn.addClickHandler(closeGameBtnClickHandler);

        for(int row=0; row<3; row++)
            for(int col=0;col<3; col++){
                gameTableButtons[row][col] = new XOCellButton(row, col);
                initXOCellButton(gameTableButtons[row][col]);
                gameTable.setWidget(row, col, gameTableButtons[row][col]);
            }
    }

    void showOpenedGameBoard(){
        gameIdLabel.setText("Game #" + gameId + " is created.");
        gameSystemMessage.setText("Wait for opponents.");
        showGameBoard();
    }

    void showGameBoard(){
        gameboardPanel.clear();
        gameboardPanel.add(gameIdLabel);
        gameboardPanel.add(BR);
        gameboardPanel.add(gameSystemMessage);
        gameboardPanel.add(BR);
        gameboardPanel.add(gameTable);
        gameboardPanel.add(BR);
        gameboardPanel.add(closeGameBtn);
        closeGameLock = false;
        RootPanel.get("gamePanel").add(gameboardPanel);
    }

    // Функция отрисовки игрового Лобби
    void showGameLobby(){

        createNewGameLabel.setStyleName("boldText");
        joinToOpenGamesLabel.setStyleName("boldText");
        createNewGameBtnPanel.setWidth("100%");
        createNewGameBtnPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        createNewGameButton.setEnabled(true);
        createNewGameButton.addClickHandler(createNewGameBtnClickHandler);

        updateOpenGamesTimer.run();
        updateOpenGamesTimer.scheduleRepeating(2000);
        updateGameLobby();
    }

    void updateGameLobby(){
        createNewGameBtnPanel.add(createNewGameButton);
        RootPanel.get("gamePanel").clear();
        RootPanel.get("gamePanel").add(createNewGameLabel);
        RootPanel.get("gamePanel").add(BR);
        RootPanel.get("gamePanel").add(createNewGameBtnPanel);
        RootPanel.get("gamePanel").add(BR);
        RootPanel.get("gamePanel").add(BR);
        // Отображаем приглашение к открытым играм, если они есть
        if(openGames.size() != 0){
            RootPanel.get("gamePanel").add(joinToOpenGamesLabel);
            RootPanel.get("gamePanel").add(BR);
            for(int i=1; i<= openGames.size(); i++)
                RootPanel.get("gamePanel").add(openGames.get(i - 1));
        }
    }

    void hideGameLobby(){
        RootPanel.get("gamePanel").clear();
        updateOpenGamesTimer.cancel();
    }

    void showUserStatsPanel(String userAccount){
        userStatsPanel.clear();
        userStatsPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
        userStatsPanel.setWidth("100%");
        userStatsPanel.add(new HTML("<hr/>"));
        title.setText("Stats of user \"" + userAccount + "\"");
        title.setStyleName("boldText");
        userStatsPanel.add(title);
        //userStatsPanel.add(BR);
        userStatsPanel.add(new Label("... в разработке ..."));
        //userStatsPanel.add(BR);
        closeUserStatsPanelBtn.addClickHandler(closeUserStatsClickHandler);
        userStatsPanel.add(closeUserStatsPanelBtn);
        RootPanel.get("userStatistic").add(userStatsPanel);
    }

    void hideUserStatsPanel(){
        RootPanel.get("userStatistic").clear();
    }


    public void initChatWidget(){
        chatPanel.setWidth("100%");
        chatMуssagesScroll.setStyleName("chatPanel");
        chatMуssagesScroll.setSize("100%", "200px");
        chatMуssagesScroll.add(chatMуssages);
        chatPanel.add(chatMуssagesScroll);
        chatNewMassageText.setWidth("100%");
        sendNewMessagePanel.setWidth("100%");
        chatNewMassageText.addKeyPressHandler(msgTextBoxKeyPressHandler);
        sendNewMessagePanel.add(chatNewMassageText);
        sendChatMessageBtn.addClickHandler(sendChatMessageBtnClickHandler);
        sendNewMessagePanel.add(sendChatMessageBtn);
        chatPanel.add(sendNewMessagePanel);
        RootPanel.get("chat").add(chatPanel);
    }

    void enableChat(){
        chatNewMassageText.setEnabled(true);
        sendChatMessageBtn.setEnabled(true);
    }

    void disableChat(){
        chatNewMassageText.setEnabled(false);
        sendChatMessageBtn.setEnabled(false);
    }


    //############################################################################################################
    //##### KEY PRESS HANDLERS ###################################################################################
    //############################################################################################################
    KeyPressHandler msgTextBoxKeyPressHandler = new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent keyPressEvent) {
            if (keyPressEvent.getCharCode() == 13)
                sendChatMessageServiceCall();
        }
    };

    KeyPressHandler loginPassTextBoxKeyPressHandler  = new KeyPressHandler() {
        @Override
        public void onKeyPress(KeyPressEvent keyPressEvent) {
            if (keyPressEvent.getCharCode() == 13)
                loginCheckServiceCall();
        }
    };

    //############################################################################################################
    //##### BUTTON'S CLICK HANDLERS ##############################################################################
    //############################################################################################################

    ClickHandler loginBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            errorLoginMessage.setText("");
            loginCheckServiceCall();
        }
    };

    ClickHandler logoutBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            myAccount = "";
            Cookies.removeCookie("user");
            showLoginPanel();
            hideGameLobby();
            disableChat();
        }
    };

    ClickHandler backToLoginBtnHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            showLoginPanel();
        }
    };

    ClickHandler resetStyleTextBox = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            ( (TextBox)event.getSource()).setStyleName("defaultTextBoxHighLight");
            registrationErrorLabel.setText("");
        }
    };

    ClickHandler newRegistrationBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            userVerticalPanel.clear();
            userVerticalPanel.setWidth("100%");
            userVerticalPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
            accountTB.addClickHandler(resetStyleTextBox);
            nameTB.addClickHandler(resetStyleTextBox);
            lastNameTB.addClickHandler(resetStyleTextBox);
            passTB.addClickHandler(resetStyleTextBox);
            confirmPassTB.addClickHandler(resetStyleTextBox);
            showRegistrationPanel();
        }
    };

    ClickHandler registrationBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {

            // Проверяем TextBox-ы формы регистрации на заполнение
            if ( ! (checkTextBoxForData(accountTB) && checkTextBoxForData(nameTB) && checkTextBoxForData(lastNameTB) && checkTextBoxForData(passTB) && checkTextBoxForData(confirmPassTB)) ){
                registrationErrorLabel.setStyleName("errorMessage");
                registrationErrorLabel.setText("Не все поля регистрационной формы заполнены");
                return;
            }

            // Проверка на соответствие TextBox-ов "пароль" и "подтверждение пароля"
            if ( !passTB.getText().equals(confirmPassTB.getText())){
                passTB.addStyleName("errorHighLight");
                confirmPassTB.addStyleName("errorHighLight");
                registrationErrorLabel.setStyleName("errorMessage");
                registrationErrorLabel.setText("Пароль не подтверждён !");
                return;
            }

            registrationServiceCall();
        }
    };

    ClickHandler XOGameCellBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            // Обновляем состояние игры
            if (iAmFirstPlayer)
                curGameXOState.setCellState( ((XOCellButton)event.getSource()).getRow(), ((XOCellButton)event.getSource()).getCol(), CellState.X );
            else
                curGameXOState.setCellState( ((XOCellButton)event.getSource()).getRow(), ((XOCellButton)event.getSource()).getCol(), CellState.O );

            // Больше мы ходить не можем
            myAction = false;
            gameSystemMessage.setText("...");
            // Отрисуем изменения на игровом поле
            updateGameboard(curGameXOState);
            // Вызываем RPC для записи обновлённого состояния игры на сервер
            writeGameStateServiceCall();
        }
    };

    ClickHandler backToGameLobbyBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            showGameLobby();
        }
    };

    ClickHandler createNewGameBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            //((Button)event.getSource()).setEnabled(false); // не даём больше создавать игр
            if (!createrNewGameLock)
                createNewGameServiceCall();
            createrNewGameLock = true;
        }
    };

    ClickHandler joinToOpenGameBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            gameId = ((OpenGameButton)event.getSource()).getGameId();
            joinToOpenGameServiceCall();
        }
    };

    ClickHandler closeGameBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            if (!closeGameLock)
                closeGameServiceCall();
            closeGameLock = true;
        }
    };

    ClickHandler userOnlineLabelClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            //Window.alert("User ckick handler");
            showUserStatsPanel(((Label)event.getSource()).getText());
        }
    };

    ClickHandler closeUserStatsClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent event) {
            hideUserStatsPanel();
        }
    };

    ClickHandler sendChatMessageBtnClickHandler = new ClickHandler() {
        @Override
        public void onClick(ClickEvent clickEvent) {
            sendChatMessageServiceCall();
        }
    };

    //############################################################################################################
    //##### ASYNC SERIVICES' CALLBACKS ##########################################################################################
    //############################################################################################################

    AsyncCallback<ErrorResult> loginCheckServiceCallback = new AsyncCallback<ErrorResult>(){
        @Override
        public void onFailure(Throwable caught) { Window.alert("RPC to checkLogin() failed."); }
        @Override
        public void onSuccess(ErrorResult result) {
            if ( ! result.getError() ){
                myAccount = result.getErrorDescription(); // если result.error = false, то в result.ErrorDescription - имя пользователя
                Cookies.setCookie("user", myAccount);
                showWelcomePanel();
                showGameLobby();
                enableChat();
            }
            else
                errorLoginMessage.setText(result.getErrorDescription());
        }
    };

    AsyncCallback<ErrorResult> registrationServiceCallback = new AsyncCallback<ErrorResult>() {
        @Override
        public void onFailure(Throwable caught) {Window.alert("registration RPC error");}
        @Override
        public void onSuccess(ErrorResult result) {
            // Ошибка в ходе регистрации
            if (result.getError())
                registrationErrorLabel.setText(result.getErrorDescription());
            else{
                showSuccessfulRegistration(result.getErrorDescription()); //  в ErrorDescription здесь - имя нового пользователя
            }
        }
    };

    AsyncCallback<List<String>> updateUsersOnlineServiceCallback = new AsyncCallback<List<String>>(){
        @Override
        public void onFailure(Throwable caught) { Window.alert("RPC timer error"); }
        @Override
        public void onSuccess(List<String> usersOnline) {
            usersOnlineList.clear();
            usersOnlinePanel.clear();
            usersOnlinePanel.setSpacing(7);
            if (usersOnline.size() > 0){
                for(String curUser : usersOnline){
                    usersOnlineList.add(new Label(curUser));
                }
                for(Label curLabel: usersOnlineList){
                    curLabel.setStyleName("userLabel");
                    curLabel.addClickHandler(userOnlineLabelClickHandler);
                    usersOnlinePanel.add(curLabel);
                }
            }
        }
    };

    AsyncCallback<GameXO_State> readGameStateServiceCallback = new AsyncCallback<GameXO_State>() {
        @Override
        public void onFailure(Throwable caught) { Window.alert("RPC readGameStateService ERROR."); }
        @Override
        public void onSuccess(GameXO_State curGameState) {
            if (curGameState != null){
                // Считали состояние игры с указанным ID
                curGameXOState = curGameState;
                // Если приняли переданный нам ход
                if (myAccount.equals(curGameXOState.getCurPlayer())){
                    myAction = true;
                    updateGameStateTimer.cancel();
                    gameSystemMessage.setText("My action");
                }
                // Если ждём своего хода
                else{
                    GameXO_WinState winState = new GameXO_WinState();

                    // Игра завершена по техническим причинам
                    if (curGameState.isGameTerminated()){
                        gameSystemMessage.setText("Opponent exit from game.Yoг are win !!!");
                        winState.setExitOpponentWin(true);
                        showWinState(winState);
                        return;
                    }

                    // Определяем закончена ли партия ?
                    winState.isGameEnds(curGameState);
                    // Если приняли выигрышное состояние игры- оппонент победил
                    if (winState.getWin()){
                        updateGameboard(curGameState);
                        showWinState(winState);
                        return;
                    }
                    // во всех остальных случаях
                    gameSystemMessage.setText("Opponent action");
                }
                updateGameboard(curGameState);
            }

            // Если мы случайно попали сюда, игры с указанным id не существует, или она по каким-то причинам удалена из базы данных - то переходим в стартовое игровое меню
            else{
                showGameLobby();
            }
        }
    };

    AsyncCallback<GameXO_WinState> writeGameStateServiceCallback = new AsyncCallback<GameXO_WinState>() {
        @Override
        public void onFailure(Throwable caught) { Window.alert("RPC writeGameStateService ERROR."); }
        @Override
        public void onSuccess(GameXO_WinState result) {
            if (result.getWin()){
                updateGameStateTimer.cancel();
                showWinState(result);
            }
            else{
                updateGameStateTimer.run(); // instant
                updateGameStateTimer.scheduleRepeating(1000); //  + periodical
            }
        }
    };

    AsyncCallback<Integer> createNewGameServiceCallback = new AsyncCallback<Integer>() {
        @Override
        public void onFailure(Throwable caught) {Window.alert("createNewGameService error !!!"); }
        @Override
        public void onSuccess(Integer result) {
            // Если игра создана
            if (result != -1){
                gameId = result;
                gameCreated = true;
                gamePlaying = false;
                iAmFirstPlayer = true;
                hideGameLobby();
                initGameboardWidget();
                showOpenedGameBoard();
                waitForOpponentTimer.scheduleRepeating(1000);
            }
            createrNewGameLock = false;// сразу на будующее разрешаем создавать новые игры
        }
    };

    AsyncCallback<List<SimpleGameInfo>> getOpenGamesServiceCallback = new AsyncCallback<List<SimpleGameInfo>>() {
        @Override
        public void onFailure(Throwable caught) { Window.alert("createNewGameService error !!!"); }
        @Override
        public void onSuccess(List<SimpleGameInfo> result) {
            openGames.clear();
            for(int i=0; i< result.size(); i++){

                // Если среди игр есть незаконченная игра с нашим участием
                if ( (result.get(i).getPlayer_1().equals(myAccount) && !result.get(i).getPlayer_2().equals("")) || result.get(i).getPlayer_2().equals(myAccount)){
                    gameId = result.get(i).getId();
                    gameIdLabel.setText("Game #" + gameId + " is running.");
                    gameCreated = true;
                    gamePlaying = true;
                    // Определяемся - на какой стороне(X / O) мы играем
                    iAmFirstPlayer = result.get(i).getPlayer_1().equals(myAccount);

                    hideGameLobby();
                    initGameboardWidget();
                    showGameBoard();
                    updateGameStateTimer.run(); // считываем состояние игры сразу же
                    updateGameStateTimer.scheduleRepeating(1000); // запускаем задачу считывания состояния игры периодически
                    return;
                }

                // Если среди открытых игр есть игра, открытая нами
                if (result.get(i).getPlayer_1().equals(myAccount) && result.get(i).getPlayer_2().equals("")){
                    gameId = result.get(i).getId();
                    gameCreated = true;
                    gamePlaying = false;
                    iAmFirstPlayer = true;
                    hideGameLobby();
                    initGameboardWidget();
                    showOpenedGameBoard();
                    updateOpenGamesTimer.cancel();
                    waitForOpponentTimer.scheduleRepeating(1000);
                    return;
                }

                // Заносим найденный игры в массив игр, отражаемых в игровом лобби
                openGames.add(new OpenGameButton(result.get(i).getGameType(), result.get(i).getId(), result.get(i).getPlayer_1()));
                openGames.get(i).addClickHandler(joinToOpenGameBtnClickHandler);
            }
            updateGameLobby();
        }
    };

    AsyncCallback<String> waitForOpponentServiceCallback = new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable caught) { Window.alert("RPC waitForOpponentServiceCallback ERROR."); }
        @Override
        public void onSuccess(String result) {
            if(result != null){
                gameOpponentAccount = result;
                waitForOpponentTimer.cancel();

                gamePlaying = true;
                //Cookies.setCookie("gamePlaying", "true");
                //Cookies.setCookie("gameId", gameId.toString());
                updateGameStateTimer.run();
            }
        }
    };

    AsyncCallback<Boolean> joinToOpenGameServiceCallback = new AsyncCallback<Boolean>() {
        @Override
        public void onFailure(Throwable caught) { Window.alert("RPC waitForOpponentServiceCallback ERROR."); }
        @Override
        public void onSuccess(Boolean result) {
            if (result){
                // Останавливаем сервис поиска открытых игр
                updateOpenGamesTimer.cancel();
                // закрываем игровое меню
                hideGameLobby();

                gameIdLabel.setText("Game #" + gameId + " is running.");
                // Отображаем игровое поле
                initGameboardWidget();
                showGameBoard();

                // Запускаем серви обновления состояния игры
                updateGameStateTimer.run();
                updateGameStateTimer.scheduleRepeating(1000);
            }
        }
    };

    AsyncCallback<Boolean> closeGameServiceCallback = new AsyncCallback<Boolean>() {
        @Override
        public void onFailure(Throwable caught){ Window.alert("RPC closeGameServiceCallback ERROR.");}
        @Override
        public void onSuccess(Boolean result) {
            if (result){
                gameId=null;
                gameOpponentAccount=null;
                updateGameStateTimer.cancel();
                waitForOpponentTimer.cancel();
                showGameLobby();
            }
        }
    };

    AsyncCallback<Void> sendChatMessageServiceCallback = new AsyncCallback<Void>() {
        @Override
        public void onFailure(Throwable throwable) {
            Window.alert("sendChatMessageService ERROR.");
            chatNewMassageText.setEnabled(true);
            sendChatMessageBtn.setEnabled(true);
        }
        @Override
        public void onSuccess(Void aVoid) {
            chatNewMassageText.setEnabled(true);
            chatNewMassageText.setText("");
            sendChatMessageBtn.setEnabled(true);
        }
    };

    AsyncCallback<String> updateChatServiceCallback = new AsyncCallback<String>() {
        @Override
        public void onFailure(Throwable throwable) {
            Window.alert("updateChatService ERROR");
        }

        @Override
        public void onSuccess(String s) {
            if (lastReadChatMsgIndex == 0)
                chatMуssages.add(new HTML("<p>Последние 5 сообщений:</p>"));

            JSONObject serverAnswer = (JSONObject) JSONParser.parseStrict(s);
            JSONArray messages = (JSONArray) serverAnswer.get("messages");
            for(int i=0; i< messages.size(); i++){
                JSONObject message = (JSONObject)messages.get(i);
                JSONString id = (JSONString)message.get("id");
                JSONString sender = (JSONString)message.get("sender");
                JSONString text = (JSONString)message.get("text");
                JSONString time = (JSONString)message.get("time");
                chatMуssages.add(new HTML("<p>[" + time.stringValue() + "] <b>" + sender.stringValue() + "</b> : " + text.stringValue() + "</p>"));
                chatMуssagesScroll.scrollToBottom();
                lastReadChatMsgIndex = Long.parseLong(id.stringValue());
            }
        }
    };


    //############################################################################################################
    //##### SERIVICES' CALLS ##########################################################################################
    //############################################################################################################

    GameServiceAsync gameService = GameService.App.getInstance();
    AuthorizationServiceAsync authorizationService = AuthorizationService.App.getInstance();
    ChatServiceAsync chatService = ChatService.App.getInstance();

    void loginCheckServiceCall(){
        authorizationService.checkLogin(loginUserTextBox.getText(), loginPassTextBox.getText(), loginCheckServiceCallback);
    }

    void registrationServiceCall(){
        authorizationService.setNewAccount(new Person(accountTB.getText(), nameTB.getText(), lastNameTB.getText(), passTB.getText()), registrationServiceCallback );
    }

    void updateUsersOnlineServiceCall(){
        authorizationService.getUsersOnline(myAccount,updateUsersOnlineServiceCallback);
    }

    void readGameStateServiceCall(){
        gameService.updateGameState( gameId, readGameStateServiceCallback);
    }

    void writeGameStateServiceCall(){
        gameService.writeGameState( gameId, myAccount, curGameXOState, writeGameStateServiceCallback);
    }

    void createNewGameServiceCall(){
        gameService.createGame(myAccount, createNewGameServiceCallback);
    }

    void getOpenGamesServiceCall(){
        gameService.getListOfOpenGames(myAccount, getOpenGamesServiceCallback);
    }

    void waitForOpponentServiceCall(){
        gameService.waitForOpponent(gameId,waitForOpponentServiceCallback);
    }

    void joinToOpenGameServiceCall(){
        gameService.joinToOpenGame(gameId,myAccount,joinToOpenGameServiceCallback);
    }

    void closeGameServiceCall(){
        gameService.closeGame(gameId, myAccount, closeGameServiceCallback);
    }

    void sendChatMessageServiceCall(){
        chatService.sendNewMesage(myAccount, chatNewMassageText.getText(), sendChatMessageServiceCallback );
        chatNewMassageText.setEnabled(false);
        sendChatMessageBtn.setEnabled(false);
    }

    void updateChatServiceCall(){
        chatService.getNewMassages(lastReadChatMsgIndex, updateChatServiceCallback);
    }

    //############################################################################################################
    //##### TIMERS ###############################################################################################
    //############################################################################################################
    Timer updateUsersOnlineTimer = new Timer() {
        @Override
        public void run() {
            updateUsersOnlineServiceCall();
        }
    };

    Timer updateGameStateTimer = new Timer(){
        @Override
        public void run() {
            readGameStateServiceCall();
        }
    };

    Timer updateOpenGamesTimer = new Timer() {
        @Override
        public void run() {
            getOpenGamesServiceCall();
        }
    };

    Timer waitForOpponentTimer = new Timer() {
        @Override
        public void run() {
            waitForOpponentServiceCall();
        }
    };

    Timer chatUpdate = new Timer() {
        @Override
        public void run() {
            updateChatServiceCall();
        }
    };


    //############################################################################################################
    //##### CUSTOM FUNCTIONS #####################################################################################
    //############################################################################################################

    // Функция проверки TextBox на пустоту
    boolean checkTextBoxForData(TextBox tb){
        // Если в TextBox нет данных
        if (tb.getText().equals("")){
            tb.addStyleName("errorHighLight");
            return false;
        }
        return true;
    }

    // Функция обновления игрового поля по состоянию GameXO_State
    public void updateGameboard(GameXO_State curState){

        CellState curCellState;

        for(int row=0; row < 3; row++){
            for(int col=0; col < 3; col++){
                curCellState = curState.getCellState(row, col);
                if (curCellState == CellState.Empty){
                    if (! myAction){
                        gameTableButtons[row][col].setEnabled(false);
                        gameTableButtons[row][col].setStyleName("ocupyXOCell");
                    }
                    else{
                        gameTableButtons[row][col].setEnabled(true);
                        gameTableButtons[row][col].setStyleName("freeXOCell");
                    }
                } else if (curCellState == CellState.X){
                    gameTableButtons[row][col].setEnabled(false);
                    gameTableButtons[row][col].setText("X");
                    gameTableButtons[row][col].setStyleName("ocupyXOCell");
                } else if (curCellState == CellState.O){
                    gameTableButtons[row][col].setEnabled(false);
                    gameTableButtons[row][col].setText("O");
                    gameTableButtons[row][col].setStyleName("ocupyXOCell");
                }
                gameTable.setWidget(row, col, gameTableButtons[row][col]);
            }
        }
    }

    // Фуцнкция отображения победного состояния
    void showWinState(GameXO_WinState winState){

        // Сообщение об окончании игры
        gameIdLabel.setText("Game #" + gameId + " over.");

        // Победа по выходу игрока ???
        if (winState.isExitOpponentWin())
            gameSystemMessage.setText("Opponent exit from game. You are win !!!");
        else{
            String winPlayer = winState.getWinPlayer();
            // Закончились игровые клетки, победителя нет
            if (winPlayer == null){
                gameSystemMessage.setText("No winners. Try again !!!");
            }
            // Есть победитель
            else{
                if (winState.getWinPlayer().equals(myAccount))
                    gameSystemMessage.setText("You are win. Congratulations !!!");
                else
                    gameSystemMessage.setText("You are lose. Don't worry !!!");

                // Выделение выигравших ячеек стилем "winXOCell"
                for(int i=0; i<3; i++)
                    gameTableButtons[winState.getWinCellRow(i)][winState.getWinCellCol(i)].setStyleName("winXOCell");
            }
        }

        gameboardPanel.remove(closeGameBtn);
        backToGameLobbyBtn.addClickHandler(backToGameLobbyBtnClickHandler);
        gameboardPanel.add(BR);
        gameboardPanel.add(backToGameLobbyBtn);

        // Удаление информации об игре
        gameCreated = false;
        gamePlaying = false;
        gameId = null;
        myAction = false;
        iAmFirstPlayer = false;
        gameOpponentAccount = null;
        gameOver = false;
        curGameXOState = null;
    }

    //============================================================================================================
    // Main Enrty-Point function
    //============================================================================================================
    public void onModuleLoad() {

        // Init
        initGameboardWidget();
        initChatWidget();
        chatUpdate.scheduleRepeating(1000);

        // Check registration by cookies
        isLogin = (myAccount = Cookies.getCookie("user")) != null;

        // Show user-info panel in case of authorization
        if ( isLogin ){
            showWelcomePanel();
            enableChat();
            showGameLobby(); // здесь найдём игры, которые мы отрыли и в которых мы участвуем
        }
        else{
            showLoginPanel();
            disableChat();
        }

        // Add panel "Users online"
        RootPanel.get("usersOnline").add(usersOnlinePanel);
        // Update users' info instantly on page load
        updateUsersOnlineTimer.run();
        // Update users' info on page periodicaly
        updateUsersOnlineTimer.scheduleRepeating(5000); //  задача на обновлнение списка пользователей онлайн
    }
}
