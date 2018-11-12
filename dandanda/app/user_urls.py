import random
import string

from db.user_ops import UserOp
from flask import request, render_template, url_for
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
    if q_usr.password == password:
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

