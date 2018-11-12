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
