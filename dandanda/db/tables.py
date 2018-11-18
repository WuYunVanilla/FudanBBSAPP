from sqlalchemy.ext.declarative import declarative_base
from sqlalchemy import Column, Integer, String, Boolean

Base = declarative_base()


class User(Base):
    __tablename__ = 'users'
    id = Column(Integer, primary_key=True)
    stdId = Column(String)
    password = Column(String)
    valid = Column(Boolean)

    def __repr__(self):
        return '%s(%r, %r)' % (self.__class__.__name__, self.stdId, self.password)


class Question(Base):
    __tablename__ = 'questions'
    id = Column(Integer, primary_key=True)
    questioner = Column(String)
    title = Column(String)
    label = Column(String)
    description = Column(String)
    isPublished = Column(Boolean)
    numFollowers = Column(Integer)
    numAnswers = Column(Integer)

    def __repr__(self):
        return '%s(%r, %r, %r, %r, %r, %r, %r, %r)' % (self.__class__.__name__, self.id, self.title,
                                                       self.label, self.description, self.questioner,
                                                       self.isPublished, self.numFollowers, self.numAnswers)


class Answer(Base):
    __tablename__ = 'answers'
    id = Column(Integer, primary_key=True)
    answerer = Column(String)
    questionID = Column(Integer)
    content = Column(String)
    numAgree = Column(Integer)
    numCollect = Column(Integer)

    def __repr__(self):
        return '%s(%r, %r, %r, %r, %r, %r)' % (self.__class__.__name__, self.id, self.answerer, self.questionID,
                                               self.content, self.numAgree, self.numCollect)
