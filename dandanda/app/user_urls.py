import random
import string

from db.user_ops import UserOp
from db.qa_ops import QAOp
from flask import request, render_template, url_for, jsonify
from email import send_email

from app import app, message as ms
from itsdangerous import URLSafeTimedSerializer


from flask_mail import Mail, Message
app.config.update(
    MAIL_SERVER='smtp.qq.com',
    MAIL_PORT=465,
    MAIL_USE_SSL=True,
    MAIL_USERNAME='1316541911',
    MAIL_PASSWORD='frnfrvnevtxjgfed')
mail = Mail(app)


@app.route('/login/')  # url params
def login():
    stdId = request.args.get('stdid')
    password = request.args.get('password')
    q_usr = UserOp.query(stdId)
    if q_usr == None:
        return ms.USER_NOT_EXIST
    if q_usr.password == password and q_usr.valid:
        return ms.LOGIN_OK
    else:
        return ms.PASSWORD_ERROR


@app.route('/register/')
def register():
    stdId = request.args.get('stdid')
    password = request.args.get('password')
    # user exists or not
    q_usr = UserOp.query(stdId)
    if q_usr != None:
        return ms.USER_EXIST
    # sent register mail
    sent_register_mail(stdId)
    # insert into usrs table, valid = False
    UserOp.insert(stdId=stdId, password=password, valid=False)
    UserOp.printf()  # debug
    return ms.REGISTER_OK


@app.route('/modify_password/')
def modify_password():
    stdId = request.args.get('stdid')
    # user exists or not
    q_usr = UserOp.query(stdId)
    if q_usr == None:
       return ms.USER_NOT_EXIST
    # generate new password
    new_pw = generate_rand_six_password()
    UserOp.modifyPassword(stdId, new_pw)
    # sent modify password mail
    sent_modify_password_mail(stdId, new_pw)
    UserOp.printf()  # debug
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
    print("in confirm: " + str(stdId))
    UserOp.confirm(stdId)
    return ms.CONFIRM


@app.route('/homepage/')
def home_page():
    ret = {}
    list1, list2 = QAOp.showQuestions(2)
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
        dict.setdefault("oneAnswer", list2[i])
        ret.setdefault("question"+str(i), dict)
    return jsonify(ret)


@app.route('/add_question/')
def add_question():
    # get parameters......
    # questioner, title, label, description, isPublished
    # QAOp.insertQuestion("15302010051", "Who are ZhangJiang F4", "Fun", "Who are ZhangJiang F4", True)
    QAOp.printQuestions()  # debug
    return ms.ADD_QUESTION_OK


@app.route('/answers_list/')
def answers_list():
    ret = {}
    q_id = request.args.get('questionId')
    list = QAOp.showAnswers(q_id)
    for i in range(0, len(list)):
        dict = {}
        a = list[i]
        dict.setdefault("answerId", a.id)
        dict.setdefault("answerer", a.answerer)
        dict.setdefault("content", a.content)
        dict.setdefault("numAgree", a.numAgree)
        dict.setdefault("numCollect", a.numCollect)
        ret.setdefault("answer"+str(i), dict)
    return jsonify(ret)


@app.route('/add_answer/')
def add_answer():
    # get parameters......
    # answerer, questionID, content
    # QAOp.insertAnswer("15302010053", 1, "they are zhuyafang, fangruiyu, yuanliping and wuyun")
    # QAOp.add_num_answers(1)
    QAOp.printQuestions()  # debug
    QAOp.printAnswers()  # debug
    return ms.ADD_ANSWER_OK


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

