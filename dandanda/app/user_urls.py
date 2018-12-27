import random
import string
import json

from app import app, message as ms
from flask_mail import Mail, Message
from itsdangerous import URLSafeTimedSerializer
from flask import request, render_template, url_for, jsonify

from db.user_ops import UserOp
from db.qa_ops import QAOp
from email import send_email

app.config.update(
    MAIL_SERVER='smtp.qq.com',
    MAIL_PORT=465,
    MAIL_USE_SSL=True,
    MAIL_USERNAME='1316541911',
    MAIL_PASSWORD='frnfrvnevtxjgfed')
mail = Mail(app)


##################################################

# login and register

##################################################


@app.route('/login/')
def login():
    UserOp.printf()
    stdId = request.args.get('stdid')
    password = request.args.get('password')
    q_usr = UserOp.query(stdId)
    if q_usr == None:
        return ms.USER_NOT_EXIST
    if q_usr.password == password and q_usr.valid:
        ret = {}
        ret.setdefault("nickname", q_usr.nickname)
        ret.setdefault("gender", q_usr.gender)
        ret.setdefault("grade", q_usr.grade)
        ret.setdefault("signature", q_usr.signature)
        ret.setdefault("photo", q_usr.photo)
        ret.setdefault("num_posting", len(q_usr.posting.split("\t")))
        ret.setdefault("num_answer", len(q_usr.answer.split("\t")))
        ret.setdefault("num_follow", len(q_usr.follow.split("\t")))
        ret.setdefault("num_collection", len(q_usr.collection.split("\t")))
        ret.setdefault("num_view", len(q_usr.view.split("\t")))
        return jsonify(ret)
    else:
        return ms.PASSWORD_ERROR


@app.route('/register/')
def register():
    stdId = request.args.get('stdid')
    password = request.args.get('password')
    q_usr = UserOp.query(stdId)
    if q_usr != None:
        return ms.USER_EXIST
    sent_register_mail(stdId)
    UserOp.insert(stdId=stdId, password=password, valid=False)
    return ms.REGISTER_OK


@app.route('/modify_password/')
def modify_password():
    stdId = request.args.get('stdid')
    q_usr = UserOp.query(stdId)
    if q_usr == None:
       return ms.USER_NOT_EXIST
    new_pw = generate_rand_six_password()
    UserOp.modifyPassword(stdId, new_pw)
    sent_modify_password_mail(stdId, new_pw)
    return ms.IGNORE


@app.route('/confirm/<token>')
def confirm(token, expiration=3600):
    serializer = URLSafeTimedSerializer("SECRET_KEY")
    try:
        email = serializer.loads(
            token,
            salt="email-confirm-key",
            max_age=expiration
        )
    except:
        return
    stdId = email[0:-13]
    UserOp.confirm(stdId)
    return ms.CONFIRM


def sent_register_mail(stdId):
    user_email = str(stdId) + "@fudan.edu.cn"
    subject = "Please confirm your email for dandanda"
    token = generate_confirmation_token(user_email)
    confirm_url = url_for('confirm', token=token, _external=True)
    html = render_template('activate.html', confirm_url=confirm_url)
    send_email(subject, user_email, html)


def sent_modify_password_mail(stdId, pawd):
    user_email = str(stdId) + "@fudan.edu.cn"
    subject = "Your new password for dandanda"
    html = render_template("modify_password.html", password=pawd)
    send_email(subject, user_email, html)


def generate_rand_six_password():
    password = random.sample(string.ascii_letters + string.digits, 6)
    return ''.join(password)


def generate_confirmation_token(email):
    serializer = URLSafeTimedSerializer("SECRET_KEY")
    return serializer.dumps(email, salt="email-confirm-key")


##################################################

# homepage

##################################################


@app.route('/homepage/')
def home_page():
    list1, list2 = QAOp.showQuestions()
    ret = get_homepage(list1, list2)
    return jsonify(ret)


@app.route('/recommend/')
def recommend():
    stdId = request.args.get('stdid')
    user = UserOp.query(stdId)
    user.view = "1\t2"
    posting_arr = user.posting.split("\t")
    follow_arr = user.follow.split("\t")
    view_arr = user.view.split("\t")
    que_arr = posting_arr + follow_arr + view_arr
    q_list = []
    for q in que_arr:
        if int(q) not in q_list:
            q_list.append(int(q))
    list1, list2 = QAOp.get_recommend(q_list)
    # get more questions to show
    que_count = QAOp.get_que_count()
    else_q_list = []
    print(que_count)
    print(else_q_list)
    for i in range(1, que_count+1):
        if i not in q_list:
            else_q_list.append(i)
    if len(else_q_list) > 0:
        _list1, _list2 = QAOp.get_recommend(else_q_list)
        ret = get_homepage(list1+_list1, list2+_list2)
    else:
        ret = get_homepage(list1, list2)
    # ret = get_homepage(list1, list2)
    return jsonify(ret)


@app.route('/search/')
def search():
    key = request.args.get('keyword')
    list1, list2 = QAOp.get_search(key)
    ret = get_homepage(list1, list2)
    return jsonify(ret)


def get_homepage(list1, list2):
    ret = {}
    for i in range(0, len(list2)):
        dict = {}
        q = list1[i]
        dict.setdefault("questionId", q.id)
        dict.setdefault("questioner", q.questioner)
        dict.setdefault("title", q.title)
        dict.setdefault("label", q.label)
        dict.setdefault("description", q.description)
        dict.setdefault("numFollowers", q.numFollowers)
        dict.setdefault("numAnswers", q.numAnswers)
        dict.setdefault("date", q.date)
        if q.numAnswers == 0:
            dict.setdefault("oneAnswer", "don't have any answer")
            dict.setdefault("answerId", -1)
        else:
            dict.setdefault("oneAnswer", list2[i].content)
            dict.setdefault("answerId", list2[i].id)
        ret.setdefault("question"+str(i), dict)
    return ret


@app.route('/add_question/', methods=['POST'])
def add_question():
    # get parameters......
    # questioner, title, label, description, isPublished
    data = request.get_data()
    j_data = json.loads(data)
    q_id = QAOp.insertQuestion(j_data["questioner"], j_data["title"], j_data["label"],
                               j_data["description"], j_data["isPublished"])
    UserOp.add_posting(j_data["questioner"], q_id)
    QAOp.printQuestions()
    UserOp.printf()
    return ms.ADD_QUESTION_OK


@app.route('/add_answer/', methods=['POST'])
def add_answer():
    # get parameters......
    # answerer, questionID, content, isPublished
    data = request.get_data()
    j_data = json.loads(data)
    a_id = QAOp.insertAnswer(j_data["answerer"], j_data["questionID"], j_data["content"], j_data["isPublished"])
    QAOp.add_num_answers(j_data["questionID"])
    UserOp.add_answer(j_data["answerer"], a_id)
    QAOp.printQuestions()
    QAOp.printAnswers()
    UserOp.printf()
    return ms.ADD_ANSWER_OK


@app.route('/answers_list/')
def answers_list():
    ret = {}
    stdId = request.args.get('stdid')
    q_id = request.args.get('queid')
    que = QAOp.getQ(q_id)
    ret.setdefault("questioner", UserOp.query(que.questioner).nickname)
    ret.setdefault("title", que.title)
    ret.setdefault("label", que.label)
    ret.setdefault("description", que.description)
    ret.setdefault("numFollowers", que.numFollowers)
    ret.setdefault("numAnswers", que.numAnswers)
    ret.setdefault("date", que.date)
    QAOp.printQuestions()
    QAOp.printAnswers()
    UserOp.add_view(stdId, q_id)
    UserOp.printf()
    list = QAOp.showAnswers(q_id)
    ret.setdefault("num", len(list))
    print(len(list))
    for i in range(0, len(list)):
        dict = {}
        a = list[i]
        dict.setdefault("answerId", a[0])
        dict.setdefault("answerer", a[1])
        dict.setdefault("answerer_name", UserOp.query(a[1]).nickname)
        dict.setdefault("content", a[2])
        dict.setdefault("numAgree", a[3])
        dict.setdefault("numCollect", a[4])
        dict.setdefault("date", a[5])
        ret.setdefault("answer"+str(i), dict)
    return jsonify(ret)


@app.route('/answer_detail/')
def answer_detail():
    ret = {}
    stdId = request.args.get('stdid')
    a_id = request.args.get('ansid')
    ans = QAOp.show_answer_detail(a_id)
    ret.setdefault("answerer", ans.answerer)
    ret.setdefault("answerer_name", UserOp.query(ans.answerer).nickname)
    ret.setdefault("questionID", ans.questionID)
    ret.setdefault("content", ans.content)
    ret.setdefault("isPublished", ans.isPublished)
    ret.setdefault("numAgree", ans.numAgree)
    ret.setdefault("numCollect", ans.numCollect)
    ret.setdefault("date", ans.date)
    return jsonify(ret)


@app.route('/add_follow/')
def add_follow():
    stdId = request.args.get('stdid')
    queId = request.args.get('queid')
    QAOp.add_num_followers(queId)
    UserOp.add_follow(stdId, queId)
    return ms.ADD_OK


@app.route('/add_agree/')
def add_agree():
    stdId = request.args.get('stdid')
    ansId = request.args.get('ansid')
    QAOp.add_num_collect(ansId)
    return ms.ADD_OK


@app.route('/add_collection/')
def add_collection():
    stdId = request.args.get('stdid')
    ansId = request.args.get('ansid')
    QAOp.add_num_collect(ansId)
    UserOp.add_collection(stdId, ansId)
    return ms.ADD_OK



##################################################

# user information

##################################################


@app.route('/modify/', methods=['POST'])
def modify():
    # get parameters......
    # stdId, nickname, gender, grade, signature, photo
    data = request.get_data()
    j_data = json.loads(data)
    UserOp.modify(j_data["stdid"], j_data["key"], j_data["value"])
    UserOp.printf()
    return ms.MODIFY_OK


@app.route('/posting/')
def posting():
    stdId = request.args.get('stdid')
    ret = {}
    q_usr = UserOp.query(stdId)
    posting = q_usr.posting
    split = posting.split("\t")
    num = len(split)
    ret.setdefault("question_number", num)
    if num == 0:
        return jsonify(ret)
    else:
        for i in range(num):
            dict = {}
            questionId = int(split[i])
            que = QAOp.show_question_detail(questionId)
            dict.setdefault("question_id", questionId)
            dict.setdefault("question", que.title)
            dict.setdefault("question_description", que.description)
            dict.setdefault("date", que.date)
            dict.setdefault("is_draft", que.isPublished)
            ret.setdefault("question"+str(i), dict)
        return jsonify(ret)


@app.route('/follow/')
def follow():
    stdId = request.args.get('stdid')
    q_usr = UserOp.query(stdId)
    follow = q_usr.follow
    return get_follow_and_view(follow)


@app.route('/view/')
def view():
    stdId = request.args.get('stdid')
    q_usr = UserOp.query(stdId)
    view = q_usr.view
    return get_follow_and_view(view)


@app.route('/answer/')
def answer():
    stdId = request.args.get('stdid')
    q_usr = UserOp.query(stdId)
    ans = q_usr.answer
    ret = {}
    split = ans.split("\t")
    num = len(split)
    ret.setdefault("answer_number", num)
    if num == 0:
        return jsonify(ret)
    else:
        for i in range(num):
            dict = {}
            answerId = int(split[i])
            ans = QAOp.show_answer_detail(answerId)
            que = QAOp.show_question_detail(ans.questionID)
            dict.setdefault("question_id", str(ans.questionID))
            dict.setdefault("question", que.title)
            dict.setdefault("answer_id", answerId)
            dict.setdefault("answer", ans.content)
            dict.setdefault("date", ans.date)
            dict.setdefault("is_draft", ans.isPublished)
            ret.setdefault("answer" + str(i), dict)
        return jsonify(ret)


@app.route('/collection/')
def collection():
    stdId = request.args.get('stdid')
    q_usr = UserOp.query(stdId)
    collec = q_usr.collection
    ret = {}
    split = collec.split("\t")
    num = len(split)
    ret.setdefault("answer_number", num)
    if num == 0:
        return jsonify(ret)
    else:
        for i in range(num):
            dict = {}
            answerId = int(split[i])
            ans = QAOp.show_answer_detail(answerId)
            que = QAOp.show_question_detail(ans.questionID)
            dict.setdefault("question_id", str(ans.questionID))
            dict.setdefault("question", que.title)
            dict.setdefault("answer_id", answerId)
            dict.setdefault("answer", ans.content)
            dict.setdefault("num_followers", que.numFollowers)
            dict.setdefault("num_answers", que.numAnswers)
            ret.setdefault("answer" + str(i), dict)
        return jsonify(ret)


def get_follow_and_view(f_or_v):
    ret = {}
    split = f_or_v.split("\t")
    num = len(split)
    ret.setdefault("question_number", num)
    if num == 0:
        return jsonify(ret)
    else:
        for i in range(num):
            if split[i] is not None:
                dict = {}
                questionId = int(split[i])
                que = QAOp.show_question_detail(questionId)
                dict.setdefault("question_id", questionId)
                dict.setdefault("question", que.title)
                dict.setdefault("num_followers", que.numFollowers)
                dict.setdefault("num_answers", que.numAnswers)
                ret.setdefault("question" + str(i), dict)
        return jsonify(ret)
